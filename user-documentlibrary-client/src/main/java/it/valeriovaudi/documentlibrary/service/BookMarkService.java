package it.valeriovaudi.documentlibrary.service;

import it.valeriovaudi.documentlibrary.model.Book;
import it.valeriovaudi.documentlibrary.model.UserBookPreferedList;
import it.valeriovaudi.documentlibrary.repository.UserBookPreferedListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonValue;
import java.io.StringReader;
import java.security.Principal;
import java.util.Optional;

/**
 * Created by Valerio on 29/06/2015.
 */
@RestController
@RequestMapping("/bookMark")
public class BookMarkService {

    @Autowired
    private UserBookPreferedListRepository userBookPreferedListRepository;

    public void setUserBookPreferedListRepository(UserBookPreferedListRepository userBookPreferedListRepository) {
        this.userBookPreferedListRepository = userBookPreferedListRepository;
    }

    @RequestMapping(value = "/{bookId}")
    public ResponseEntity getBookMark(@PathVariable("bookId") String bookId, Principal principal){
        UserBookPreferedList byUserName = userBookPreferedListRepository.findByUserName(principal.getName());
        Book bookAux = getBookById(byUserName, bookId);
        return bookAux!=null ? ResponseEntity.ok(bookAux.getPageBookMark()) : ResponseEntity.notFound().build();
    }

    @RequestMapping(value = "/{bookId}", method = RequestMethod.PUT)
    public ResponseEntity updateBookMark(@PathVariable("bookId") String bookId, @RequestBody String body, Principal principal){
        UserBookPreferedList byUserName = userBookPreferedListRepository.findByUserName(principal.getName());
        Book bookAux = getBookById(byUserName, bookId);

        if(bookAux!=null){
            int page;
            JsonObject jsonObject = Json.createReader(new StringReader(body)).readObject();
            if(jsonObject.get("page").getValueType().compareTo(JsonValue.ValueType.NUMBER)==0){
                page = jsonObject.getInt("page");
            } else {
                page = Integer.parseInt(jsonObject.getString("page"));
            }
            bookAux.setPageBookMark(page);
            userBookPreferedListRepository.save(byUserName);
        }

        return ResponseEntity.noContent().build();
    }

    private Book getBookById(UserBookPreferedList byUserName,String bookId){
        final Book[] book = {null};
        Optional.ofNullable(byUserName)
                .ifPresent(userBookPreferedList -> book[0] = userBookPreferedList.getBooksReadList().stream()
                        .filter(filteredBook -> filteredBook.getBookId().equals(bookId))
                            .findFirst().orElse(null));
        return book[0];
    }
}

