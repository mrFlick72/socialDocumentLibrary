package it.valeriovaudi.documentlibrary.service;

import it.valeriovaudi.documentlibrary.model.Book;
import it.valeriovaudi.documentlibrary.model.UserBookPreferedList;
import it.valeriovaudi.documentlibrary.model.factory.BookFactory;
import it.valeriovaudi.documentlibrary.repository.BookRepository;
import it.valeriovaudi.documentlibrary.repository.UserBookPreferedListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.transaction.Transactional;
import java.io.StringReader;
import java.security.Principal;
import java.util.List;

import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;

/**
 * Created by Valerio on 23/06/2015.
 */
@RestController
@RequestMapping("/bookService")
@Transactional
public class BookService {

    @Value("${searchBookService.searchBookService.baseUrl}")
    private String searchBookBaseUrl;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserBookPreferedListRepository userBookPreferedListRepository;

    @Autowired
    private RestTemplate searchBookServiceRestTemplate;

    @Autowired
    private BookFactory bookFactory;

    public void setBookRepository(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public void setSearchBookBaseUrl(String searchBookBaseUrl) {
        this.searchBookBaseUrl = searchBookBaseUrl;
    }

    public void setUserBookPreferedListRepository(UserBookPreferedListRepository userBookPreferedListRepository) {
        this.userBookPreferedListRepository = userBookPreferedListRepository;
    }

    public void setSearchBookServiceRestTemplate(RestTemplate searchBookServiceRestTemplate) {
        this.searchBookServiceRestTemplate = searchBookServiceRestTemplate;
    }

    public void setBookFactory(BookFactory bookFactory) {
        this.bookFactory = bookFactory;
    }

    @RequestMapping("/books")
    public ResponseEntity getUserBooks(@RequestParam("bookName") String bookName, Principal principal){
        UserBookPreferedList userBookPreferredList = userBookPreferedListRepository.findByUserName(principal.getName());
        JsonArrayBuilder jsonArrayBuildeResult = Json.createArrayBuilder();
        if(userBookPreferredList!=null){
            jsonArrayBuildeResult = userBookPreferredList.getBooksReadList().stream()
                        .<Book>filter(bookAux -> {
                            String bookNameAux = bookAux.getBookName();
                            return bookNameAux != null && bookNameAux.toUpperCase().contains(bookName.toUpperCase());
                        })
                        .<JsonArrayBuilder>map(mapBookAux -> Json.createArrayBuilder().add(bookFactory.bookListJsonFactory(mapBookAux.getBookId())))
                        .reduce(Json.createArrayBuilder(), (finalJsonArrayBuilder, jsonArrayBuilder) -> finalJsonArrayBuilder.add(jsonArrayBuilder.build().get(0)));
        }

        return ResponseEntity.ok(jsonArrayBuildeResult.build().toString());
    }

    @RequestMapping("/bookUserList")
    public ResponseEntity getUserBookList(Principal principal){
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

        UserBookPreferedList userBookPreferredList = userBookPreferedListRepository.findByUserName(principal.getName());
        if(userBookPreferredList!=null){
            List<Book> booksReadList = userBookPreferredList.getBooksReadList();
            for (Book book : booksReadList) {
                arrayBuilder.add(bookFactory.bookListJsonFactory(book.getBookId()));
            }
        }

        return ResponseEntity.ok(arrayBuilder.build().toString());
    }

    @RequestMapping(value = "/bookUserList/{bookId}",method = RequestMethod.DELETE)
    public ResponseEntity<Void> removeBookFromBookUserList(Principal principal, @PathVariable("bookId") String bookId){
        UserBookPreferedList byUserName = userBookPreferedListRepository.findByUserName(principal.getName());

        Book userBook = byUserName.getBooksReadList().stream().filter(bookAux -> bookAux.getBookId().equals(bookId)).findFirst().orElse(null);

        if(userBook!=null){
            byUserName.getBooksReadList().remove(userBook);
        }

        return ResponseEntity.noContent().build();
    }

    @RequestMapping
    public ResponseEntity searchBook(@RequestParam(value = "bookName",required = false,defaultValue = "") String bookName,
                                     @RequestParam(value = "searchTags",required = false,defaultValue = "") List<String> searchTags){
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        JsonObject searchBookJsonValue;
        String bookId;
        // search section
        ResponseEntity<String> searchBookResponseEntity = searchBookFromSearchService(bookName, searchTags);

        JsonArray searchBookJsonValues = Json.createReader(new StringReader(searchBookResponseEntity.getBody())).readArray();

        for(int i = 0 ; i < searchBookJsonValues.size() ; i++){
            searchBookJsonValue = searchBookJsonValues.getJsonObject(i);
            bookId = searchBookJsonValue.getString("bookId");
            arrayBuilder.add(bookFactory.bookListJsonFactory(bookId));
        }

        return ResponseEntity.ok(arrayBuilder.build().toString());
    }

    public ResponseEntity<String> searchBookFromSearchService(String bookName, List<String> searchTags){
        // search section
        StringBuilder query = new StringBuilder();
        if(bookName!=null){
            query.append("bookName=").append(bookName);
        }
        if(searchTags!=null && !searchTags.isEmpty()){
            StringBuilder searchTagQuery = new StringBuilder();
            searchTagQuery.append("searchTags=");
            for (String searchTag : searchTags) {
                searchTagQuery.append(searchTag);
                searchTagQuery.append(",");
            }

            // delete the last comma
            searchTagQuery.deleteCharAt(searchTagQuery.length()-1);
            query.append(";").append(searchTagQuery.toString());
        }

        UriComponents build = fromHttpUrl(searchBookBaseUrl).queryParam("q", query.toString()).build();
        return ResponseEntity.ok(searchBookServiceRestTemplate.getForObject(build.toUriString(), String.class));
    }
}
