package it.valeriovaudi.documentlibrary.repository;

import it.valeriovaudi.documentlibrary.MongoGridFsApplicationTests;
import it.valeriovaudi.documentlibrary.builder.BookBuilder;
import it.valeriovaudi.documentlibrary.model.Book;
import it.valeriovaudi.documentlibrary.model.Page;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static it.valeriovaudi.documentlibrary.model.BookTestFactory.createBook;
import static it.valeriovaudi.documentlibrary.model.BookTestFactory.createPages;
import static java.lang.String.format;
import static org.junit.Assert.*;

/**
 * Created by Valerio on 29/04/2015.
 */
public class BookRepositoryTests extends MongoGridFsApplicationTests {
    String bookId = null;

    @Autowired
    BookRepository bookRepository;

    @Test
    public void saveTest() throws IOException {
        Book newBook = createBook();

        Book save = bookRepository.save(newBook,createPages());
        bookId = save.getId();
        LOGGER.info(save.toString());
        LOGGER.info(bookId);
        assertNotNull(save.getId());
    }

    @Test
    public void saveFailureTest() throws IOException {
        Book save = bookRepository.save(BookBuilder.newBookBuilder().build(),null);
        bookId = save.getId();
        LOGGER.info(save.toString());
        LOGGER.info(bookId);
        assertNotNull(save.getId());
    }

    @Test
    public void readTest() throws IOException {
        saveTest();
        int pageCount = 19;
        Page read = bookRepository.read(bookId, pageCount);
        LOGGER.info(read.toString());
        assertNotNull(read);
        assertEquals(format("page%s.jpg", pageCount), read.getFileName());
    }

    @Test
    public void failureReadPageRangeWithNullBookTest() throws IOException {
        List<Page> read = bookRepository.read("", 1,50);
        LOGGER.info(String.valueOf(read.size()));
        assertNotNull(read);
        assertEquals(0, read.size());
    }

    @Test
    public void failureReadPageRangeWithNotNullBookTest() throws IOException {
        Book save = bookRepository.save(BookBuilder.newBookBuilder().build(), new ArrayList<>());

        List<Page> read = bookRepository.read(save.getId(), 1,50);
        LOGGER.info(String.valueOf(read.size()));
        assertNotNull(read);
        assertEquals(0, read.size());
    }

    @Test
    public void readPageRangeTest() throws IOException {
        saveTest();
        List<Page> read = bookRepository.read(bookId, 1,50);
        LOGGER.info(String.valueOf(read.size()));
        assertNotNull(read);
        assertEquals(50, read.size());
    }

    @Test
    public void readBookTest() throws IOException {
        saveTest();

        int startPage = 5;
        int pageWindowSize = 10;
        int i = 0;

        Book book = bookRepository.readBook(bookId,startPage,pageWindowSize);
        LOGGER.info("Book: " + book);
        assertNotNull(book);
        assertSame(10, book.getPageId().size());

        int checkIndex = startPage;
        for (Map.Entry<Integer, Object> integerObjectEntry : book.getPageId().entrySet()) {
            LOGGER.info(integerObjectEntry.toString());
            assertSame(checkIndex, integerObjectEntry.getKey());
            checkIndex++;
        }
    }

    @Test
    public void readAllBooksTest() throws IOException {
        for(int i = 0 ; i < 20 ; i++){
            saveTest();
        }

        for (int i = 0 ; i  < 5 ; i++){
            List<Book> books = bookRepository.readAllBooks(i, 4);
            LOGGER.info("books: " + books);
            assertSame(books.size(), 4);
        }

        List<Book> books = bookRepository.readAllBooks(5, 4);
        LOGGER.info("books: " + books);
        assertSame(books.size(), 0);
    }

    @Test
    public void countAllBooksTest() throws IOException {
        saveTest();
        long countAllBooks = bookRepository.countAllBooks();
        LOGGER.info("countAllBooks: " + countAllBooks);
        assertSame(countAllBooks, 1L);
    }

}
