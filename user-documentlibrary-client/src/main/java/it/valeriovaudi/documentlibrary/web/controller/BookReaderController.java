package it.valeriovaudi.documentlibrary.web.controller;

import it.valeriovaudi.documentlibrary.model.Book;
import it.valeriovaudi.documentlibrary.model.UserBookPreferedList;
import it.valeriovaudi.documentlibrary.model.builder.BookBuilder;
import it.valeriovaudi.documentlibrary.repository.BookRepository;
import it.valeriovaudi.documentlibrary.repository.UserBookPreferedListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.LocalDateTime;

/**
 * Created by Valerio on 18/05/2015.
 */

@Controller
public class BookReaderController {

    @Autowired
    private UserBookPreferedListRepository userBookPreferedListRepository;

    @Autowired
    private BookRepository bookRepository;

    public void setBookRepository(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public void setUserBookPreferedListRepository(UserBookPreferedListRepository userBookPreferedListRepository) {
        this.userBookPreferedListRepository = userBookPreferedListRepository;
    }

    @Transactional
    @RequestMapping("/bookReader")
    public String initBookReader(@RequestParam("bookId") String bookId,@RequestParam("bookName") String bookName, Model model,Principal principal){
        model.addAttribute("bookId", bookId);
        model.addAttribute("templatePath","bookReader/bookReader");
        model.addAttribute("template","content");
        model.addAttribute("withToolBoox",true);

        UserBookPreferedList byUserName = userBookPreferedListRepository.findByUserName(principal.getName());
        Book userBook = byUserName.getBooksReadList().stream().filter(bookAux -> bookAux.getBookId().equals(bookId)).findFirst().orElse(null);
        if(userBook==null){
            Book book = bookRepository.save(BookBuilder.newBookBuilder().bookId(bookId).pageBookMark(1).bookName(bookName).localDate(LocalDateTime.now()).build());
            byUserName.getBooksReadList().add(book);
        } else {
            userBook.setLocalDate(LocalDateTime.now());
            bookRepository.save(userBook);
        }

        return "index";
    }
}
