package it.valeriovaudi.documentlibrary.model.factory;

import it.valeriovaudi.documentlibrary.model.builder.BookUserInterfaceDTOBuilder;
import it.valeriovaudi.documentlibrary.service.BookMetadataService;
import it.valeriovaudi.documentlibrary.service.BookRepositoryService;
import it.valeriovaudi.documentlibrary.service.FeedBackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rx.Observable;

import javax.json.JsonObject;
import java.util.List;

/**
 * Created by Valerio on 30/06/2015.
 */
@Component
public class BookFactory {

    protected Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private BookMetadataService bookMetadataService;

    @Autowired
    private BookRepositoryService bookRepositoryService;

    @Autowired
    private FeedBackService feedBackService;

    public void setBookMetadataService(BookMetadataService bookMetadataService) {
        this.bookMetadataService = bookMetadataService;
    }

    public void setBookRepositoryService(BookRepositoryService bookRepositoryService) {
        this.bookRepositoryService = bookRepositoryService;
    }

    public void setFeedBackService(FeedBackService feedBackService) {
        this.feedBackService = feedBackService;
    }

    public JsonObject bookListJsonFactory(String bookId) {
        BookUserInterfaceDTOBuilder bookUserInterfaceDTOBuilder = BookUserInterfaceDTOBuilder.newBookUserInterfaceDTOBuilder();

        String bookMetadataResponseEntity = bookMetadataService.getSocialMetadataByBookId(bookId).toBlocking().single().getBody();
        String bookRepositoryResponseEntity = bookRepositoryService.getBookDataById(bookId).toBlocking().single().getBody();


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
