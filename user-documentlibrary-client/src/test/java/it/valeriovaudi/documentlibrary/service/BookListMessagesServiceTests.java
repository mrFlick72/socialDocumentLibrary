package it.valeriovaudi.documentlibrary.service;

import it.valeriovaudi.documentlibrary.UserDocumentLibraryClientApplicationAbstractTests;
import org.junit.Test;
import org.springframework.web.util.UriComponentsBuilder;


/**
 * Created by Valerio on 26/08/2015.
 */
public class BookListMessagesServiceTests extends UserDocumentLibraryClientApplicationAbstractTests {

    @Test
    public void getBookListMessagesTest() throws Exception {
        LOGGER.info(genericGet(UriComponentsBuilder.fromPath("/bookList/messages").build()));
    }

}
