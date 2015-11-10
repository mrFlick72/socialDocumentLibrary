package it.valeriovaudi.documentlibrary.model.factory;

import it.valeriovaudi.documentlibrary.model.builder.BookUserInterfaceDTOBuilder;
import it.valeriovaudi.documentlibrary.service.FeedBackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.json.JsonObject;

import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;

/**
 * Created by Valerio on 30/06/2015.
 */
@Component
public class BookFactory {

    @Value("${searchBookService.searchBookService.baseUrl}")
    private String searchBookBaseUrl;

    @Value("${bookSocialMetadataService.feedBackService.baseUrl}")
    private String bookSocialMetadataBaseUrl;

    @Value("${bookRepositoryService.bookServiceEndPoint.baseUrl}")
    private String bookRepositoryService;

    @Autowired
    private RestTemplate bookMetadataServiceRestTemplate;

    @Autowired
    private RestTemplate bookRepositoryServiceRestTemplate;

    @Autowired
    private FeedBackService feedBackService;

    public void setFeedBackService(FeedBackService feedBackService) {
        this.feedBackService = feedBackService;
    }

    public void setSearchBookBaseUrl(String searchBookBaseUrl) {
        this.searchBookBaseUrl = searchBookBaseUrl;
    }

    public void setBookSocialMetadataBaseUrl(String bookSocialMetadataBaseUrl) {
        this.bookSocialMetadataBaseUrl = bookSocialMetadataBaseUrl;
    }

    public void setBookRepositoryService(String bookRepositoryService) {
        this.bookRepositoryService = bookRepositoryService;
    }

    public void setBookMetadataServiceRestTemplate(RestTemplate bookMetadataServiceRestTemplate) {
        this.bookMetadataServiceRestTemplate = bookMetadataServiceRestTemplate;
    }

    public void setBookRepositoryServiceRestTemplate(RestTemplate bookRepositoryServiceRestTemplate) {
        this.bookRepositoryServiceRestTemplate = bookRepositoryServiceRestTemplate;
    }

    public JsonObject bookListJsonFactory(String bookId){
        BookUserInterfaceDTOBuilder bookUserInterfaceDTOBuilder = BookUserInterfaceDTOBuilder.newBookUserInterfaceDTOBuilder();
        String bookMetadataResponseEntity = bookMetadataServiceRestTemplate.getForObject(fromHttpUrl(String.format("%s/bookId/%s/data", bookSocialMetadataBaseUrl, bookId)).toUriString(), String.class);
        String bookRepositoryResponseEntity = bookRepositoryServiceRestTemplate.getForObject(fromHttpUrl(String.format("%s/book/%s.json?startRecord=1&pageSize=1", bookRepositoryService, bookId)).toUriString(), String.class);

        bookUserInterfaceDTOBuilder
                .bookId(bookRepositoryResponseEntity)
                .thumbnail(bookRepositoryResponseEntity)
                .bookName(bookRepositoryResponseEntity)
                .description(bookRepositoryResponseEntity)
                .feedBack(bookMetadataResponseEntity)
                .userFeedBacks(feedBackService.getUserFeedBack(bookId).getBody());

        return bookUserInterfaceDTOBuilder.buildJson();
    }
}
