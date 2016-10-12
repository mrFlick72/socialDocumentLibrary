package it.valeriovaudi.documentlibrary.service;

/**
 * Created by Valerio on 05/06/2015.
 */

import it.valeriovaudi.documentlibrary.repository.SearchMetadaTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/searchMetadaTag")
public class SearchMetadaTagService {

    @Autowired
    private SearchMetadaTagRepository searchMetadaTagRepository;

    public void setSearchMetadaTagRepository(SearchMetadaTagRepository searchMetadaTagRepository) {
        this.searchMetadaTagRepository = searchMetadaTagRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<String>> getSearchMetadaTag(){
        return ResponseEntity.ok(searchMetadaTagRepository.getSearchMetadaTag().getMetadata());
    }
}
