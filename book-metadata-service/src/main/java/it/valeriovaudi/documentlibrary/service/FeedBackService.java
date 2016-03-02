package it.valeriovaudi.documentlibrary.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.valeriovaudi.documentlibrary.model.FeedBack;
import it.valeriovaudi.documentlibrary.repository.FeedBackRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.fromMethodName;

/**
 * Created by Valerio on 05/06/2015.
 */

@RestController
@RequestMapping("/feedBack")
public class FeedBackService {

    public static final String USER_NAME_KEY = "userName";
    public static final String BOOK_ID_KEY   = "bookId";

    private static final Logger LOGGER  = LoggerFactory.getLogger(FeedBackService.class);


    @Autowired
    private FeedBackRepository feedBackRepository;

    @Autowired
    private FeedBackFactory feedBackFactory;

    public void setFeedBackRepository(FeedBackRepository feedBackRepository) {
        this.feedBackRepository = feedBackRepository;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> ConstraintViolationExceptionHandler(Exception e) {
        LOGGER.error(e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    /* read methods */
    /**
     * @param bookIdUserNameList : [{userName:'....',bookId:'....'} , ... ,{userName:'....',bookId:'....'}]*/
    @RequestMapping("/batch")
    public ResponseEntity<String> getByUserBookIdBatchMode(@RequestBody List<Map<String,String>> bookIdUserNameList,
                                                           @RequestParam(required = false) List<String> filterQuery) throws IOException {
        String body;
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        JsonObject jsonObject;

        for (Map<String, String> stringStringMap : bookIdUserNameList) {
            body = getFeedBack(stringStringMap.get(USER_NAME_KEY),stringStringMap.get(BOOK_ID_KEY), filterQuery).getBody();
            if(body != null && !body.trim().equals("")){
                jsonObject = Json.createReader(new StringReader(body)).readObject();
                if(!jsonObject.isEmpty()){
                    arrayBuilder.add(jsonObject);
                }
            }
        }

        return ResponseEntity.ok(arrayBuilder.build().toString());
    }

    @RequestMapping("/{userName}/{bookId}/data")
    public ResponseEntity<String> getFeedBack(@PathVariable("userName") String userName,
                                              @PathVariable("bookId") String bookId,
                                              @RequestParam(required = false) List<String> filterQuery){
        List<FeedBack> feedBack = feedBackRepository.getFeedBack(userName, bookId);
        String feedBackFilteredJson = feedBackFactory.createFeedBackFilteredJson(filterQuery, feedBack.size() > 0 ? feedBack.get(0) : null);
        return ResponseEntity.ok(feedBackFilteredJson);
    }

    @RequestMapping("/userName/{userName}/data")
    public ResponseEntity<String> getFeedBackByUser(@PathVariable("userName") String userName,
                                                    @RequestParam(required = false) List<String> filterQuery){
        List<FeedBack> feedBack = feedBackRepository.getFeedBack(userName, null);
        return ResponseEntity.ok(feedBackFactory.createFeedBackFilteredJsonArray(filterQuery,feedBack));
    }

    @RequestMapping("/bookId/{bookId}/data")
    public ResponseEntity<String> getFeedBackByBook(@PathVariable("bookId") String bookId,
                                                    @RequestParam(required = false) List<String> filterQuery){
        List<FeedBack> feedBack = feedBackRepository.getFeedBack(null, bookId);
        return ResponseEntity.ok(feedBackFactory.createFeedBackFilteredJsonArray(filterQuery,feedBack));
    }

    @RequestMapping("/{feedBackId}")
    public ResponseEntity<String> getFeedBackById(@PathVariable("feedBackId") String feedBackId,
                                                  @RequestParam(required = false) List<String> filterQuery){
        FeedBack one = feedBackRepository.findOne(feedBackId);
        return ResponseEntity.ok(feedBackFactory.createFeedBackFilteredJson(filterQuery, one));
    }

    @RequestMapping(value = "/{feedBackId}", method = RequestMethod.PUT)
    public ResponseEntity<Void> updateFeedBack(@PathVariable("feedBackId") String feedBackId, @Valid @RequestBody FeedBack feedBack){
        FeedBack one = feedBackRepository.findOne(feedBackId);
        return Optional.ofNullable(one).map(feedBackAux -> {
            if(feedBack.getDateTime()==null){
                feedBack.setDateTime(LocalDateTime.now());
            }

            feedBack.setId(feedBackId);
            feedBackRepository.save(feedBack);
            return ResponseEntity.noContent().build();
        }).orElse(ResponseEntity.notFound().build());
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> addFeedBack(@Valid @RequestBody FeedBack feedBack){
        if(feedBack.getDateTime()==null){
            feedBack.setDateTime(LocalDateTime.now());
        }

        FeedBack save = feedBackRepository.save(feedBack);
        URI getFeedBackLocation = fromMethodName(FeedBackService.class, "getFeedBackById", new Object[]{save.getId(),null}).build().toUri();
        return ResponseEntity.created(getFeedBackLocation).build();
    }

    @Component
    static class FeedBackFactory {
        private static final Logger LOGGER  = LoggerFactory.getLogger(FeedBackFactory.class);

        @Autowired
        private ObjectMapper objectMapper;

        public String createFeedBackFilteredJson(List<String> filter, FeedBack feedBack){
            PropertyDescriptor descriptor;
            Map<String,Object> filteredObject = new HashMap<>();
            String result = "";
            if(feedBack!=null){
                if(filter!=null && filter.size() > 0){
                    for (String fieldName : filter){
                        try {
                            descriptor = new PropertyDescriptor(fieldName, FeedBack.class);
                            filteredObject.put(fieldName, descriptor.getReadMethod().invoke(feedBack));
                        } catch (IntrospectionException |
                                InvocationTargetException |
                                IllegalAccessException e) {
                            LOGGER.error(e.getMessage());
                        }
                    }
                }
                try {
                    return filter!=null ? objectMapper.writeValueAsString(filteredObject) : objectMapper.writeValueAsString(feedBack);
                } catch (JsonProcessingException e) {
                    LOGGER.error(e.getMessage());
                }
            } else {
                result = "{}";
            }

            return result;
        }

        public String createFeedBackFilteredJsonArray(List<String> filter, List<FeedBack> feedBacks){
            StringBuilder filteredObject = new StringBuilder("[");

            if(feedBacks!=null){
                int i = 0;
                for (FeedBack feedBack : feedBacks) {
                    filteredObject.append(createFeedBackFilteredJson(filter,feedBack));

                    if(i < feedBacks.size()-1){
                        filteredObject.append(",");
                    }
                    i++;
                }

            }
            filteredObject.append("]");
            return filteredObject.toString();
        }
    }
}
