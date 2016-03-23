package it.valeriovaudi.documentlibrary.service;

import it.valeriovaudi.documentlibrary.model.DocumentLibraryUser;
import it.valeriovaudi.documentlibrary.model.builder.FeedBackJsonBuilder;
import it.valeriovaudi.documentlibrary.repository.DocumentLibraryUserRepository;
import it.valeriovaudi.documentlibrary.utility.JsonUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import java.io.StringReader;
import java.net.URI;
import java.security.Principal;

import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;

/**
 * Created by Valerio on 24/06/2015.
 */

@RestController
@RequestMapping("/bookService/feedBack")
public class FeedBackService extends AbstractService {

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
    public ResponseEntity<String> getUserFeedBack(@PathVariable("bookId") String bookId) {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

        JsonArray jsonValues = Json.createReader(new StringReader(bookMetadataService.getSocialMetadataByBookId(bookId).toBlocking().single().getBody())).readArray();
        FeedBackJsonBuilder feedBackJsonBuilder;
        DocumentLibraryUser byUserName;
        JsonObject jsonObjectAux;

        for (int i = 0; i < jsonValues.size(); i++) {
            jsonObjectAux = jsonValues.getJsonObject(i);
            feedBackJsonBuilder = FeedBackJsonBuilder.newFeedBackJsonBuilder(jsonObjectAux);
            byUserName = documentLibraryUserRepository.findByUserName(jsonObjectAux.getString("userName"));
            feedBackJsonBuilder.userFirstNameAndLastName(byUserName.getFirstName(), byUserName.getLastName())
                    .feadbackTitle(JsonUtility.getValueFromJson(jsonObjectAux, "feadbackTitle"))
                    .feadbackBody(JsonUtility.getValueFromJson(jsonObjectAux, "feadbackBody"));

            arrayBuilder.add(feedBackJsonBuilder.buildJson());
        }

        return ResponseEntity.ok(arrayBuilder.build().toString());
    }


    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity createFeedBack(@RequestBody String body, Principal principal) {
        URI uri = fromHttpUrl(bookSocialMetadataBaseUrl).build().toUri();
        return bookMetadataServiceRestTemplate.exchange(uri,
                HttpMethod.POST,
                RequestEntity.post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(getBodyJsonString(body, principal.getName())),
                Void.class);
    }

    @RequestMapping(value = "/{feedBackId}", method = RequestMethod.PUT)
    public ResponseEntity updateFeedBack(@PathVariable("feedBackId") String feedBackId, @RequestBody String body, Principal principal) {
        URI uri = fromHttpUrl(String.format("%s/%s", bookSocialMetadataBaseUrl, feedBackId)).build().toUri();
        return bookMetadataServiceRestTemplate.exchange(uri,
                HttpMethod.PUT,
                RequestEntity.put(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(getBodyJsonString(body, principal.getName())),
                Void.class);
    }

    private String getBodyJsonString(String body, String userName) {
        JsonObject jsonObject = FeedBackJsonBuilder.newFeedBackJsonBuilder(body)
                .userName(userName)
                .buildJson();
        return jsonObject.toString();
    }
}
