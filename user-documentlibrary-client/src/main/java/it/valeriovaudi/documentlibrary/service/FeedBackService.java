package it.valeriovaudi.documentlibrary.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.valeriovaudi.documentlibrary.model.DocumentLibraryUser;
import it.valeriovaudi.documentlibrary.model.factory.UiJsonFactory;
import it.valeriovaudi.documentlibrary.repository.DocumentLibraryUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.security.Principal;
import java.util.*;

import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;

/**
 * Created by Valerio on 24/06/2015.
 */

@RestController
@RequestMapping("/bookService/feedBack")
public class FeedBackService extends AbstractService{

    @Value("${bookSocialMetadataService.feedBackService.baseUrl}")
    private String bookSocialMetadataBaseUrl;

    @Autowired
    private DocumentLibraryUserRepository documentLibraryUserRepository;

    @Autowired
    @LoadBalanced
    private RestTemplate bookMetadataServiceRestTemplate;

    public void setBookSocialMetadataBaseUrl(String bookSocialMetadataBaseUrl) {
        this.bookSocialMetadataBaseUrl = bookSocialMetadataBaseUrl;
    }

    public void setBookMetadataServiceRestTemplate(RestTemplate bookMetadataServiceRestTemplate) {
        this.bookMetadataServiceRestTemplate = bookMetadataServiceRestTemplate;
    }

    public void setDocumentLibraryUserRepository(DocumentLibraryUserRepository documentLibraryUserRepository) {
        this.documentLibraryUserRepository = documentLibraryUserRepository;
    }

    @RequestMapping(value = "/userFeedBack/{bookId}", method = RequestMethod.GET)
    public ResponseEntity<String> getUserFeedBack(@PathVariable("bookId") String bookId) {
        URI uri = fromHttpUrl(String.format("%s/bookId/%s/data", bookSocialMetadataBaseUrl, bookId)).build().toUri();
        List<Map<String, Object>> boby = bookMetadataServiceRestTemplate.getForEntity(uri, List.class).getBody();
        List<Map> reduce = boby.parallelStream().map(stringStringMap -> {
            DocumentLibraryUser user = documentLibraryUserRepository.findByUserName(String.valueOf(stringStringMap.get("userName")));
            return Arrays.asList(UiJsonFactory.newUiJsonFactory(stringStringMap)
                    .trasformProperty("userName", "firstNameAndLastName", String.format("%s %s", user.getFirstName(), user.getFirstName()))
                    .build());
        }).sequential()
          .reduce(new ArrayList<>(boby.size()), (maps, maps2) -> {
              maps.addAll(maps2);
              return maps;
          });

        try {
            return ResponseEntity.ok(objectMapper.writeValueAsString(reduce));
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("{}");
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity createFeedBack(@RequestBody String body, Principal principal) {
        String errorMessage = "";
        try {
            Map map = objectMapper.readValue(body, HashMap.class);
            String requestBody = objectMapper.writeValueAsString(UiJsonFactory.newUiJsonFactory(map)
                    .putProperty("userName", principal.getName())
                    .build());
            URI uri = fromHttpUrl(bookSocialMetadataBaseUrl).build().toUri();
            return bookMetadataServiceRestTemplate.exchange(uri,
                    HttpMethod.POST,
                    RequestEntity.post(uri)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(requestBody),
                    Void.class);
            } catch (IOException e) {
                errorMessage+=e.getMessage();
            }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
    }

    @RequestMapping(value = "/{feedBackId}", method = RequestMethod.PUT)
    public ResponseEntity updateFeedBack(@PathVariable("feedBackId") String feedBackId, @RequestBody String body, Principal principal) {
        String errorMessage = "";
        try{
            Map map = objectMapper.readValue(body, HashMap.class);
            String requestBody = objectMapper.writeValueAsString(UiJsonFactory.newUiJsonFactory(map)
                    .putProperty("userName", principal.getName())
                    .build());

            URI uri = fromHttpUrl(String.format("%s/%s", bookSocialMetadataBaseUrl, feedBackId)).build().toUri();
            return bookMetadataServiceRestTemplate.exchange(uri,
                    HttpMethod.PUT,
                    RequestEntity.put(uri)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(requestBody),
                    Void.class);
            } catch (IOException e) {
                errorMessage+=e.getMessage();
            }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
    }
}