package it.valeriovaudi.documentlibrary.notify.controller;

import it.valeriovaudi.documentlibrary.endpoint.BookServiceEndpoint;
import it.valeriovaudi.documentlibrary.notify.repository.HistoryNotifyEntryRepository;
import it.valeriovaudi.documentlibrary.notify.service.HistoryNotifyEntryService;
import it.valeriovaudi.documentlibrary.web.model.BookModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Valerio on 16/09/2015.
 */
@RestController
@RequestMapping("/historyNotifyEntity")
public class HistoryNotifyEntityRestController {

    @Autowired
    private HistoryNotifyEntryRepository historyNotifyEntryRepository;

    @Autowired
    private HistoryNotifyEntryService historyNotifyEntryService;

    @Autowired
    private BookServiceEndpoint bookServiceEndpoint;

    public void setBookServiceEndpoint(BookServiceEndpoint bookServiceEndpoint) {
        this.bookServiceEndpoint = bookServiceEndpoint;
    }

    public void setHistoryNotifyEntryService(HistoryNotifyEntryService historyNotifyEntryService) {
        this.historyNotifyEntryService = historyNotifyEntryService;
    }

    public void setHistoryNotifyEntryRepository(HistoryNotifyEntryRepository historyNotifyEntryRepository) {
        this.historyNotifyEntryRepository = historyNotifyEntryRepository;
    }

    @RequestMapping(value = "/{bookId}/pubishBook", method = RequestMethod.PUT)
    public ResponseEntity publishBook(@PathVariable("bookId") String bookId){
        BookModel bookModel = new BookModel();
        bookModel.setPublished(true);
        bookServiceEndpoint.updateBook(bookId,bookModel);
        return historyNotifyEntryService.deleteNotifyByBookId(bookId) ?
                ResponseEntity.noContent().build():
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during the notification cancellation");
    }

    @RequestMapping(value = "/{bookId}/deleteBook", method = RequestMethod.DELETE)
    public ResponseEntity deleteBook(@PathVariable("bookId") String bookId){
        bookServiceEndpoint.deleteBook(bookId);
        return historyNotifyEntryService.deleteNotifyByBookId(bookId) ?
                ResponseEntity.noContent().build():
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during the notification cancellation");
    }

    @RequestMapping(value = "/{bookId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteNotify(@PathVariable("bookId") String bookId){
        return historyNotifyEntryService.deleteNotifyByBookId(bookId) ?
                ResponseEntity.noContent().build():
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during the notification cancellation");

    }

    @RequestMapping
    public ResponseEntity findAllNotify(){
        return ResponseEntity.ok(historyNotifyEntryRepository.findAll());
    }
}
