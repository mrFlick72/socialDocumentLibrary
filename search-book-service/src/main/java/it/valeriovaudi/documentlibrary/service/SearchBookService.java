package it.valeriovaudi.documentlibrary.service;

import it.valeriovaudi.documentlibrary.model.SearchIndex;
import it.valeriovaudi.documentlibrary.repository.SearchIndexRepository;
import it.valeriovaudi.documentlibrary.repository.SearchIndexRepositoryMongoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.annotation.PostConstruct;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import java.io.StringReader;
import java.net.URI;
import java.util.*;

/**
 * Created by Valerio on 09/06/2015.
 */
@RestController
@RequestMapping("/searchBookIndex")
public class SearchBookService {

    @Autowired
    private SearchIndexRepository searchIndexRepository;

    private Map<String,Class> mapping;

    public void setMapping(Map<String, Class> mapping) {
        this.mapping = mapping;
    }

    public void setSearchIndexRepository(SearchIndexRepository searchIndexRepository) {
        this.searchIndexRepository = searchIndexRepository;
    }

    @PostConstruct
    private void init(){
        this.mapping = new HashMap<>();
        mapping.put("bookName",String.class);
        mapping.put("searchTags",List.class);
    }
    /* insert methods */
//    SearchIndex saveIndex(I bookId,String bookName,boolean published, String... searchTags);
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> saveIndex(@RequestBody SearchIndex searchIndex){
        return putMethodsResponseEntityFactory(HttpStatus.CREATED,searchIndexRepository.saveIndex(searchIndex));
    }

    /* update methods */
//    SearchIndex publishSearchBookIndex(I bookId, boolean published);
    @RequestMapping(value = "/{bookId}/publishBook",method = RequestMethod.PUT)
    public ResponseEntity<Void> publishSearchBookIndex(@PathVariable(value = "bookId") String bookId){
        SearchIndex searchIndex = searchIndexRepository.publishSearchBookIndex(bookId, true);
        return putMethodsResponseEntityFactory(HttpStatus.NO_CONTENT,searchIndex);
    }

    @RequestMapping(value = "/{bookId}/unPublishBook",method = RequestMethod.PUT)
    public ResponseEntity<Void> unPublishSearchBookIndex(@PathVariable(value = "bookId") String bookId){
        return putMethodsResponseEntityFactory(HttpStatus.NO_CONTENT,searchIndexRepository.publishSearchBookIndex(bookId, false));
    }

//    SearchIndex addSearchTags(I bookId, String...searchTags);
    @RequestMapping(value = "/{bookId}/addSearchTags",method = RequestMethod.PUT)
    public ResponseEntity<Void> addSearchTags(@PathVariable(value = "bookId") String bookId,
                                              @RequestBody String[] searchTags){
        return putMethodsResponseEntityFactory(HttpStatus.NO_CONTENT,searchIndexRepository.addSearchTags(bookId, searchTags));
    }

//    SearchIndex removeSearchTags(I bookId, String...searchTags);
    @RequestMapping(value = "/{bookId}/removeSearchTags",method = RequestMethod.PUT)
    public ResponseEntity<Void> removeSearchTags(@PathVariable(value = "bookId") String bookId,
                                                 @RequestBody String[] searchTags){
        return putMethodsResponseEntityFactory(HttpStatus.NO_CONTENT,searchIndexRepository.removeSearchTags(bookId, searchTags));
    }

    @RequestMapping(value = "/{bookId}",method = RequestMethod.PUT)
    public ResponseEntity<Void> updateSearchIndex(@PathVariable(value = "bookId") String bookId,
                                                  @RequestBody String body){
        SearchIndex searchIndexById = searchIndexRepository.findSearchIndexById(bookId);
        JsonObject readBody = Json.createReader(new StringReader(body)).readObject();

        if(readBody.containsKey("published")){
            searchIndexById.setPublished(readBody.getBoolean("published"));
        }

        if(readBody.containsKey("searchTags")){
            JsonArray searchTags = readBody.getJsonArray("searchTags");
            List<String> searchTagsValues = new ArrayList<>();
            for(int i = 0 ; i < searchTags.size(); i++){
                searchTagsValues.add(searchTags.getString(i));
            }
            searchIndexById.setSearchTags(searchTagsValues);
        }

        return putMethodsResponseEntityFactory(HttpStatus.NO_CONTENT,searchIndexRepository.saveIndex(searchIndexById));
    }

    /* read methods*/
//    SearchIndex findSearckIndexById(I bookId);
    @RequestMapping("/{bookId}")
    public ResponseEntity findSearchIndexById(@PathVariable(value = "bookId") String bookId){
        SearchIndex searckIndexById = searchIndexRepository.findSearchIndexById(bookId);
        return searckIndexById != null ? ResponseEntity.ok(searchIndexRepository.findSearchIndexById(bookId)) : ResponseEntity.notFound().build();
    }

//    List<SearchIndex> findSearchIndexByMetadata(String bookName,String... searchTags);
//    List<SearchIndex> findAllSearchIndex(int page,int pageSize);
    @RequestMapping
    public ResponseEntity<List<SearchIndex>> findSearchIndex(@RequestParam(value = "q",required = false,defaultValue = "") String query,
                                                             @RequestParam(value = "page", defaultValue = SearchIndexRepositoryMongoImpl.EMPTY_PAGE_STRING_PARAMITER,required = false) int page,
                                                             @RequestParam(value = "pageSize", defaultValue = SearchIndexRepositoryMongoImpl.EMPTY_PAGE_STRING_PARAMITER,required = false) int pageSize){

        List<SearchIndex> result;
        Map<String, Object> paramiters = searchIndexQueryParser(query,mapping);
        if(paramiters.size() > 0 ){
            String bookName = (String) paramiters.get("bookName");
            List<String> querySearchTags = (List<String>) paramiters.get("searchTags");
            List<String> searchTags = querySearchTags !=null ? querySearchTags : new ArrayList<>();

            String[] searchMetadataArray = searchTags.toArray(new String[searchTags.size()]);
            result = searchIndexRepository.findSearchIndexByMetadata(bookName, searchMetadataArray);
        } else {
            result = searchIndexRepository.findAllSearchIndex(page,pageSize);
        }

        return ResponseEntity.ok(result);
    }

    @RequestMapping(value = "/{bookId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteSearchIndex(@PathVariable("bookId") String bookId){
        return searchIndexRepository.deleteSearchIndex(bookId) ?
                ResponseEntity.noContent().build() :
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    private ResponseEntity<Void> putMethodsResponseEntityFactory(HttpStatus httpStatus,SearchIndex searchIndex){
        if(searchIndex != null){
            URI location = MvcUriComponentsBuilder.fromMethodName(SearchBookService.class, "findSearchIndexById", searchIndex.getBookId()).build().toUri();
            return ResponseEntity.status(httpStatus).location(location).build();
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private Map<String,Object> searchIndexQueryParser(String query,Map<String,Class> mapping){
        Map<String,Object> map = new HashMap<>();
        String[] split = query.split(";");
        Arrays.asList(split).stream()
                .forEach(strings -> {
                    String[] keyValueItem = strings.trim().split("=");
                    if(keyValueItem.length == 2){
                        map.put(keyValueItem[0].trim(),
                                valueAdapter(keyValueItem[1].trim(),
                                             keyValueItem[0].trim(),
                                             mapping));
                    }
                });
        return map;
    }

    private Object valueAdapter(String value,String keyValue,Map<String,Class> mapping){
        Class aClass = mapping.get(keyValue);
        Object result=null;
        if(aClass != null){
            if(aClass == String.class){
                result = value;
            } else if(aClass == List.class) {
                result =  Arrays.asList(value.split(","));
            }else if (aClass == Integer.class){
                result = new Integer(value);
            }
        }
        return result;
    }
}

