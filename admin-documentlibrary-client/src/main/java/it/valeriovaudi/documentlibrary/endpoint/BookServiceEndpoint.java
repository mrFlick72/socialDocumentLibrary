package it.valeriovaudi.documentlibrary.endpoint;

import it.valeriovaudi.documentlibrary.notify.service.HistoryNotifyEntryService;
import it.valeriovaudi.documentlibrary.web.model.BookMasterDTO;
import it.valeriovaudi.documentlibrary.web.model.BookModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.messaging.MessageChannel;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.json.*;
import java.io.StringReader;
import java.net.URI;

/**
 * Created by Valerio on 17/06/2015.
 */
@RestController
@RequestMapping("/bookService")
public class BookServiceEndpoint {

    @Autowired
    @LoadBalanced
    private RestTemplate bookRepositoryServiceRestTemplate;

    @Autowired
    @LoadBalanced
    private RestTemplate searchBookServiceRestTemplate;

    @Autowired
    private MessagingTemplate messagingTemplate;

    @Value("${bookRepositoryService.bookServiceEndPoint.baseUrl}")
    private String bookRepositoryServicBaseUrl;

    @Value("${searchBookService.searchBookService.baseUrl}")
    private String searchBookServiceBaseUrl;

    @Autowired
    @Qualifier("uploadBookInChannel")
    private MessageChannel uploadBookInChannel;

    public void setMessagingTemplate(MessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void setBookRepositoryServiceRestTemplate(RestTemplate bookRepositoryServiceRestTemplate) {
        this.bookRepositoryServiceRestTemplate = bookRepositoryServiceRestTemplate;
    }

    public void setSearchBookServiceRestTemplate(RestTemplate searchBookServiceRestTemplate) {
        this.searchBookServiceRestTemplate = searchBookServiceRestTemplate;
    }

    public void setUploadBookInChannel(MessageChannel uploadBookInChannel) {
        this.uploadBookInChannel = uploadBookInChannel;
    }

    public void setBookRepositoryServicBaseUrl(String bookRepositoryServicBaseUrl) {
        this.bookRepositoryServicBaseUrl = bookRepositoryServicBaseUrl;
    }

    public void setSearchBookServiceBaseUrl(String searchBookServiceBaseUrl) {
        this.searchBookServiceBaseUrl = searchBookServiceBaseUrl;
    }

    @RequestMapping("/{resourcesId}")
    public ResponseEntity<Void> readBookDetails(@PathVariable("resourcesId") String resourcesId){
        return null;
    }

    @RequestMapping
    public ResponseEntity<String> readAddBookDetails(){
        String forObject = searchBookServiceRestTemplate.getForObject(String.format("%s?page=-1&pageSize=-1",searchBookServiceBaseUrl), String.class);

        JsonArray searchBookIndexs = Json.createReader(new StringReader(forObject)).readArray();
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        JsonObject searchBookIndex;
        JsonObject bookDetails;
        for(int i = 0 ; i < searchBookIndexs.size() ; i++){
            searchBookIndex = searchBookIndexs.getJsonObject(i);
            bookDetails = Json.createReader(new StringReader(bookRepositoryServiceRestTemplate.getForObject(String.format(bookRepositoryServicBaseUrl + "/book/%s", searchBookIndex.getString("bookId")), String.class))).readObject();

            arrayBuilder.add(Json.createObjectBuilder()
                            .add("bookId", searchBookIndex.getString("bookId"))
                            .add("name", searchBookIndex.getString("bookName"))
                            .add("author", bookDetails.getString("author"))
                            .add("description",bookDetails.getString("description"))
                            .add("metadata",searchBookIndex.getJsonArray("searchTags"))
                            .add("published", searchBookIndex.getBoolean("published"))
                            .add("delete", false));
        }
        return ResponseEntity.ok(arrayBuilder.build().toString());
    }


    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> saveBookDetails(@RequestBody BookMasterDTO bookMasterDTO){
        messagingTemplate.convertAndSend(uploadBookInChannel, bookMasterDTO);
        return ResponseEntity.accepted().build();
    }


    @RequestMapping(value = "/{resourcesId}", method = RequestMethod.PUT)
    public ResponseEntity<Void> updateBook(@PathVariable("resourcesId") String resourcesId,
                                           @RequestBody BookModel bookModel){
        String bookRepositoryBaseURl = String.format("%s/book/%s",bookRepositoryServicBaseUrl,resourcesId);
        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
        boolean isCallabel = false;
        if(bookModel.getAuthor()!=null){
            isCallabel = true;
            objectBuilder.add("author",bookModel.getAuthor());
        }
        if(bookModel.getDescription()!=null){
            isCallabel = true;
            objectBuilder.add("description",bookModel.getDescription());
        }

        if(isCallabel) {
            bookRepositoryServiceRestTemplate.put(String.format(bookRepositoryBaseURl,resourcesId),objectBuilder.build().toString());
        }

        String searchBookIndexBaseURl = String.format("%s/%s",searchBookServiceBaseUrl,resourcesId);
        objectBuilder.add("published",bookModel.isPublished());

        if(bookModel.getMetadata()!=null){
            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
            for (String tag : bookModel.getMetadata()) {
                arrayBuilder.add(tag);
            }

            objectBuilder.add("searchTags",arrayBuilder);
        }

        searchBookServiceRestTemplate.put(String.format(searchBookIndexBaseURl,resourcesId),objectBuilder.build().toString());
        return ResponseEntity.noContent().build();
    }


    @RequestMapping(value = "/{resourcesId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteBook(@PathVariable("resourcesId") String resourcesId){
        String searchBookIndexBaseURl = String.format("%s/%s", searchBookServiceBaseUrl, resourcesId);
        return searchBookServiceRestTemplate.exchange(RequestEntity.delete(URI.create(String.format(searchBookIndexBaseURl, resourcesId))).build(), Void.class);
    }
}
