package it.valeriovaudi.documentlibrary.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.valeriovaudi.documentlibrary.model.DocumentLibraryUser;
import it.valeriovaudi.documentlibrary.model.factory.UiJsonFactory;
import it.valeriovaudi.documentlibrary.repository.DocumentLibraryUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.json.*;
import java.io.IOException;
import java.io.StringReader;
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

    @Autowired
    private BookMetadataService bookMetadataService;

    public void setBookSocialMetadataBaseUrl(String bookSocialMetadataBaseUrl) {
        this.bookSocialMetadataBaseUrl = bookSocialMetadataBaseUrl;
    }

    public void setBookMetadataServiceRestTemplate(RestTemplate bookMetadataServiceRestTemplate) {
        this.bookMetadataServiceRestTemplate = bookMetadataServiceRestTemplate;
    }

    public void setDocumentLibraryUserRepository(DocumentLibraryUserRepository documentLibraryUserRepository) {
        this.documentLibraryUserRepository = documentLibraryUserRepository;
    }

    @RequestMapping(value = "/userFeedBasck/{bookId}", method = RequestMethod.GET)
    public ResponseEntity<String> getUserFeedBack(@PathVariable("bookId") String bookId){
     /*   URI uri = fromHttpUrl(String.format("%s/bookId/%s/data",bookSocialMetadataBaseUrl,bookId)).build().toUri();
        ResponseEntity<String> forEntity = bookMetadataServiceRestTemplate.getForEntity(uri, String.class);*/

        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

        JsonArray jsonValues = Json.createReader(new StringReader(bookMetadataService.getSocialMetadataByBookId(bookId).toBlocking().single())).readArray();
        FeedBackJsonBuilder feedBackJsonBuilder;
        DocumentLibraryUser byUserName;
        JsonObject jsonObjectAux;

        for(int i = 0 ; i < jsonValues.size() ; i++){
            jsonObjectAux = jsonValues.getJsonObject(i);
            feedBackJsonBuilder = FeedBackJsonBuilder.newFeedBackJsonBuilder(jsonObjectAux);
            byUserName = documentLibraryUserRepository.findByUserName(jsonObjectAux.getString("userName"));
            feedBackJsonBuilder.userFirstNameAndLastName(byUserName.getFirstName(),byUserName.getLastName())
                    .feadbackTitle(JsonUtility.getValueFromJson(jsonObjectAux,"feadbackTitle"))
                    .feadbackBody(JsonUtility.getValueFromJson(jsonObjectAux, "feadbackBody"));

            arrayBuilder.add(feedBackJsonBuilder.buildJson());
        }

        return ResponseEntity.ok(arrayBuilder.build().toString());
    }



    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity createFeedBack(@RequestBody String body, Principal principal){
        URI uri = fromHttpUrl(bookSocialMetadataBaseUrl).build().toUri();
        return bookMetadataServiceRestTemplate.exchange(uri,
                HttpMethod.POST,
                RequestEntity.post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(getBodyJsonString(body,principal.getName())),
                Void.class);
    }

    @RequestMapping(value = "/{feedBackId}", method = RequestMethod.PUT)
    public ResponseEntity updateFeedBack(@PathVariable("feedBackId")String feedBackId, @RequestBody String body, Principal principal){
        URI uri = fromHttpUrl(String.format("%s/%s", bookSocialMetadataBaseUrl, feedBackId)).build().toUri();
        return bookMetadataServiceRestTemplate.exchange(uri,
                HttpMethod.PUT,
                RequestEntity.put(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(getBodyJsonString(body, principal.getName())),
                Void.class);
    }

    private String getBodyJsonString(String body,String userName){
        JsonObject jsonObject = FeedBackJsonBuilder.newFeedBackJsonBuilder(body)
                .userName(userName)
                .buildJson();
        return jsonObject.toString();
    }

    static class FeedBackJsonBuilder{
        private JsonObjectBuilder jsonObjectBuilder;
        private JsonObject master;
        private Set<String> actualFilds = new HashSet<>();
        private Map<String,String> frontEndKeysMap;
        private String[] frontEndKeys = new String[] {"id","bookId","userName","score","title","body","firstNameAndLastName"};

        public FeedBackJsonBuilder() {
            frontEndKeysMap = new HashMap<>();

            frontEndKeysMap.put("id","id");
            frontEndKeysMap.put("bookId","bookId");
            frontEndKeysMap.put("userName","userName");
            frontEndKeysMap.put("score","score");
            frontEndKeysMap.put("title","feadbackTitle");
            frontEndKeysMap.put("body","feadbackBody");
            frontEndKeysMap.put("firstNameAndLastName","firstNameAndLastName");
        }

        private void setJsonObjectBuilder(JsonObjectBuilder jsonObjectBuilder) {
            this.jsonObjectBuilder = jsonObjectBuilder;
        }

        private void setMaster(JsonObject master) {
            this.master = master;
        }

        public static FeedBackJsonBuilder newFeedBackJsonBuilder(JsonObject master){
            FeedBackJsonBuilder jsonObjectBuilder = new FeedBackJsonBuilder();
            jsonObjectBuilder.setJsonObjectBuilder(Json.createObjectBuilder());

            jsonObjectBuilder.setMaster(master);

            return jsonObjectBuilder;
        }

        public static FeedBackJsonBuilder newFeedBackJsonBuilder(String body){
            FeedBackJsonBuilder jsonObjectBuilder = new FeedBackJsonBuilder();
            jsonObjectBuilder.setJsonObjectBuilder(Json.createObjectBuilder());

            jsonObjectBuilder.setMaster(Json.createReader(new StringReader(body)).readObject());

            return jsonObjectBuilder;
        }

        public FeedBackJsonBuilder id(String id){
            jsonObjectBuilder.add("id", id);
            actualFilds.add("id");
            return this;
        }

        public FeedBackJsonBuilder bookId(String bookId){
            jsonObjectBuilder.add("bookId",bookId);
            actualFilds.add("bookId");
            return this;
        }

        public FeedBackJsonBuilder userName(String userName){
            jsonObjectBuilder.add("userName", userName);
            actualFilds.add("userName");
            return this;
        }

        public FeedBackJsonBuilder score(String score){
            jsonObjectBuilder.add("score",score);
            actualFilds.add("score");
            return this;
        }

        public FeedBackJsonBuilder feadbackTitle(String feadbackTitle){
            jsonObjectBuilder.add("feadbackTitle", feadbackTitle);
            actualFilds.add("title");
            return this;
        }

        public FeedBackJsonBuilder feadbackBody(String feadbackBody){
            jsonObjectBuilder.add("feadbackBody",feadbackBody);
            actualFilds.add("body");
            return this;
        }

        public FeedBackJsonBuilder userFirstNameAndLastName(String firstName,String lastName){
            jsonObjectBuilder.add("firstNameAndLastName",String.format("%s %s",firstName, lastName));
            actualFilds.add("firstNameAndLastName");
            return this;
        }

        public JsonObject buildJson(){
            for (String key : frontEndKeys) {
                if(!actualFilds.contains(key)){
                    if(!String.valueOf(JsonUtility.getValueFromJson(master,key)).trim().equals("")){
                        jsonObjectBuilder.add(frontEndKeysMap.get(key),JsonUtility.getValueFromJson(master,key));
                    }
                }
            }
            return jsonObjectBuilder.build();
        }
    }
}
class JsonUtility {
    public static String getValueFromJson(JsonObject currentJsonObject,String key){
        String result = "";
        if(currentJsonObject.containsKey(key) ){
            switch (currentJsonObject.get(key).getValueType()){
                case STRING:
                    result = currentJsonObject.getString(key);
                    break;
                case NUMBER:
                    result = String.valueOf(currentJsonObject.getInt(key));
                    break;
                case NULL:
                    result = "";
                    break;

            }
        }
        return result;
    }


}