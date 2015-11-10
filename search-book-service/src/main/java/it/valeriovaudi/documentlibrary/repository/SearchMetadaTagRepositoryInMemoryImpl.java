package it.valeriovaudi.documentlibrary.repository;

import it.valeriovaudi.documentlibrary.model.SearchMetadaTag;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Valerio on 05/06/2015.
 */
@Repository
public class SearchMetadaTagRepositoryInMemoryImpl implements SearchMetadaTagRepository {

    private static SearchMetadaTag searchMetadaTag = newSearchMetadaTag();

    private static SearchMetadaTag newSearchMetadaTag(){
        SearchMetadaTag searchMetadaTag = new SearchMetadaTag();
        Resource source = new ClassPathResource("metadataSource.csv");
        List<String> stringList;
        try {
            try(BufferedReader bufferedReader = Files.newBufferedReader(source.getFile().toPath())){
                stringList = bufferedReader.lines().map(s1 -> Arrays.asList(s1.split(","))).reduce(new ArrayList<>(), (strings, strings2) -> {
                    strings.addAll(strings2);
                    return strings;
                });
                searchMetadaTag.setMetadata(stringList);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return searchMetadaTag;
    };

    @Override
    public SearchMetadaTag addSearchMetadata(String searchMetadata) {
        searchMetadaTag.getMetadata().add(searchMetadata);
        return searchMetadaTag;
    }

    @Override
    public SearchMetadaTag removeSearchMetadata(String searchMetadata) {
        searchMetadaTag.getMetadata().remove(searchMetadata);
        return searchMetadaTag;
    }

    @Override
    public SearchMetadaTag getSearchMetadaTag() {
        return searchMetadaTag;
    }

    @Override
    public boolean containsSearchMetadaTag(String tag) {
        return searchMetadaTag.getMetadata().contains(tag);
    }
}
