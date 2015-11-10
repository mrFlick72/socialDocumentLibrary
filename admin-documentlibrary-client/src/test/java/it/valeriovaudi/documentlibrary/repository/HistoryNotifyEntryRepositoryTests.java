package it.valeriovaudi.documentlibrary.repository;

import it.valeriovaudi.documentlibrary.AdminDocumentlibraryClientApplicationTests;
import it.valeriovaudi.documentlibrary.notify.model.HistoryNotifyEntry;
import it.valeriovaudi.documentlibrary.notify.repository.HistoryNotifyEntryRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

/**
 * Created by Valerio on 16/09/2015.
 */
public class HistoryNotifyEntryRepositoryTests extends AdminDocumentlibraryClientApplicationTests {

    @Autowired
    HistoryNotifyEntryRepository historyNotifyEntryRepository;

    String bookId;
    HistoryNotifyEntry historyNotifyEntry;

    @Before
    public void setUp(){
        bookId = UUID.randomUUID().toString();
        historyNotifyEntry = new HistoryNotifyEntry();
        historyNotifyEntry.setBookName("Libro di prova");
        historyNotifyEntry.setBookId(bookId);
    }

    @Test
    public void deleteByBookIdTest(){
        historyNotifyEntryRepository.save(historyNotifyEntry);
        historyNotifyEntryRepository.deleteByBookId(bookId);

        HistoryNotifyEntry byBookId = historyNotifyEntryRepository.findByBookId(bookId);

        Assert.assertNull(byBookId);
    }

    @Test
    public void findByBookIdTest(){
        historyNotifyEntryRepository.save(historyNotifyEntry);
        HistoryNotifyEntry byBookId = historyNotifyEntryRepository.findByBookId(bookId);

        Assert.assertNotNull(byBookId);
        Assert.assertEquals(byBookId.getBookId(),historyNotifyEntry.getBookId());
        Assert.assertEquals(byBookId.getBookName(),historyNotifyEntry.getBookName());
    }
}
