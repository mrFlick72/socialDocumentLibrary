package it.valeriovaudi.documentlibrary.service;

import it.valeriovaudi.documentlibrary.model.Book;
import it.valeriovaudi.documentlibrary.model.UserBookPreferedList;
import it.valeriovaudi.documentlibrary.model.factory.BookFactory;
import it.valeriovaudi.documentlibrary.repository.BookRepository;
import it.valeriovaudi.documentlibrary.repository.UserBookPreferedListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.*;
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
import java.util.Optional;
import java.util.stream.Stream;

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
    private UserBookPreferedListRepository userBookPreferedListRepository;

    @Autowired
    @LoadBalanced
    private RestTemplate searchBookServiceRestTemplate;

    @Autowired
    private BookFactory bookFactory;

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
        final JsonArrayBuilder[] jsonArrayBuildeResult = {Json.createArrayBuilder()};
        Optional.ofNullable(userBookPreferredList).ifPresent(userBookPreferedList ->
                jsonArrayBuildeResult[0] = userBookPreferredList.getBooksReadList().stream()
                        .<Book>filter(bookAux -> {
                            String bookNameAux = bookAux.getBookName();
                            return bookNameAux != null && bookNameAux.toUpperCase().contains(bookName.toUpperCase());
                        })
                        .<JsonArrayBuilder>map(mapBookAux -> Json.createArrayBuilder().add(bookFactory.bookListJsonFactory(mapBookAux.getBookId())))
                        .reduce(Json.createArrayBuilder(), (jsonArrayBuilder, jsonArrayBuilder2) -> jsonArrayBuilder.add(jsonArrayBuilder2.build().getJsonObject(0))));
        return ResponseEntity.ok(jsonArrayBuildeResult[0].build().toString());
    }

    @RequestMapping("/bookUserList")
    public ResponseEntity getUserBookList(Principal principal){
        final JsonArrayBuilder[] arrayBuilder = {Json.createArrayBuilder()};
        Optional.ofNullable(userBookPreferedListRepository.findByUserName(principal.getName()))
                .ifPresent(userBookPreferedListRepository -> arrayBuilder[0] = userBookPreferedListRepository.getBooksReadList().stream()
                .map(book -> Json.createArrayBuilder().add(bookFactory.bookListJsonFactory(book.getBookId())))
                .reduce(Json.createArrayBuilder(), (jsonArrayBuilder, jsonArrayBuilder2) -> jsonArrayBuilder.add(jsonArrayBuilder2.build().getJsonObject(0))));

        return ResponseEntity.ok(arrayBuilder[0].build().toString());
    }

    @RequestMapping(value = "/bookUserList/{bookId}",method = RequestMethod.DELETE)
    public ResponseEntity<Void> removeBookFromBookUserList(Principal principal, @PathVariable("bookId") String bookId){
        UserBookPreferedList byUserName = userBookPreferedListRepository.findByUserName(principal.getName());
        byUserName.getBooksReadList().stream()
                .filter(bookAux -> bookAux.getBookId().equals(bookId))
                .findFirst()
                .ifPresent(book -> byUserName.getBooksReadList().remove(book));

        return ResponseEntity.noContent().build();
    }

    @RequestMapping
    public ResponseEntity searchBook(@RequestParam(value = "bookName",required = false,defaultValue = "") String bookName,
                                     @RequestParam(value = "searchTags",required = false,defaultValue = "") List<String> searchTags){
        ResponseEntity<String> searchBookResponseEntity = searchBookFromSearchService(bookName, searchTags);
        JsonArrayBuilder arrayBuilder = Json.createReader(new StringReader(searchBookResponseEntity.getBody())).readArray().stream()
                .map(jsonValue -> Json.createArrayBuilder().add(bookFactory.bookListJsonFactory(((JsonObject) jsonValue).getString("bookId"))))
                .reduce(Json.createArrayBuilder(), (jsonArrayBuilder, jsonArrayBuilder2) -> jsonArrayBuilder.add(jsonArrayBuilder2.build().getJsonObject(0)));
        return ResponseEntity.ok(arrayBuilder.build().toString());
    }

    public ResponseEntity<String> searchBookFromSearchService(String bookName, List<String> searchTags){
        StringBuilder query = new StringBuilder();

        Optional.ofNullable(bookName).ifPresent(bookNameValue -> query.append("bookName=").append(bookNameValue));
        Optional.ofNullable(searchTags)
                .ifPresent(searchTagsValues -> query.append(";searchTags=").append(searchTagsValues.stream()
                        .map(searchTagValue -> new StringBuilder().append(searchTagValue).append(","))
                        .reduce(new StringBuilder(), StringBuilder::append)));
        UriComponents build = fromHttpUrl(searchBookBaseUrl).queryParam("q", query.substring(0, query.length() - 1)).build();
        return ResponseEntity.ok(searchBookServiceRestTemplate.exchange(build.toUri(), HttpMethod.GET, RequestEntity.get(build.toUri()).accept(MediaType.APPLICATION_JSON).build(),String.class).getBody());
    }
}
