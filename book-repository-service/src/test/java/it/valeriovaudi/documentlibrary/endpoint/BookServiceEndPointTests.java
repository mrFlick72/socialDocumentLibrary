package it.valeriovaudi.documentlibrary.endpoint;

import it.valeriovaudi.documentlibrary.MongoGridFsApplicationTests;
import it.valeriovaudi.documentlibrary.model.*;
import it.valeriovaudi.documentlibrary.repository.BookRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.util.UriComponents;

import javax.jms.Message;
import java.util.List;

import static it.valeriovaudi.documentlibrary.model.BookTestFactory.createBook;
import static it.valeriovaudi.documentlibrary.model.BookTestFactory.createPages;
import static org.junit.Assert.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.web.util.UriComponentsBuilder.fromPath;

/**
 * Created by Valerio on 30/04/2015.
 */
public class BookServiceEndPointTests extends MongoGridFsApplicationTests {
    @Autowired
    BookRepository bookRepository;
    int pageNumber = 1;

    @Test
    public void getAllBook() throws Exception {
        UriComponents uriComponents = fromPath("/bookService/book").queryParam("pageNumber", 0).queryParam("pageSize", 10).build();

        LOGGER.info(uriComponents.toUri().toString());
        String genericGet = genericGet(uriComponents);

        LOGGER.info(genericGet);
        assertNotEquals(genericGet, "");
    }

    @Test
    public void getSingePageTest() throws Exception {
        Book book = createBook();
        List<Page> page = createPages();
        Book save = bookRepository.save(book, page);

        UriComponents uriComponents = fromPath("/bookService/{bookId}/pageData/{pageNumber}").buildAndExpand(save.getId(), pageNumber);
        String genericGet = genericGet(uriComponents);

        LOGGER.info(genericGet);
        assertNotEquals(genericGet, "");
    }

    @Test
    public void getSingePageByteTest() throws Exception {
        Book book = createBook();
        List<Page> page = createPages();
        Book save = bookRepository.save(book, page);

        UriComponents uriComponents = fromPath("/bookService/{bookId}/page/{pageNumber}").buildAndExpand(save.getId(), pageNumber);
        String genericGet = genericGet(uriComponents);

        LOGGER.info(genericGet);
        assertNotEquals(genericGet, "");
    }

    @Test
    public void saveBookTest() throws Exception {
        PdfBookMaster activeMqPdfBookMaster = PdfBookMasterTestFactory.pdfBookMaster(BookTestFactory.testBookWithExtensions);
        UriComponents uriComponents = fromPath("/bookService/book").build();

        mockMvc.perform(fileUpload(uriComponents.toUri())
                .file((MockMultipartFile) activeMqPdfBookMaster.getBookFile())
                .param("bookName", activeMqPdfBookMaster.getBookName())
                .param("author", activeMqPdfBookMaster.getAuthor())
                .param("description", activeMqPdfBookMaster.getDescription())
                .accept(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isAccepted());

        LOGGER.info("please wait for the job completes");
        Message receiveMessage = jmsTemplate.receive("createBookResultQueue");
        LOGGER.info(receiveMessage.toString());
    }
}
