package it.valeriovaudi.documentlibrary.repository;

import it.valeriovaudi.documentlibrary.SearchBookServiceApplicationTests;
import it.valeriovaudi.documentlibrary.model.SearchMetadaTag;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Valerio on 05/06/2015.
 * test of the SearchMetadaTagRepository in memory implementation
 * @see SearchMetadaTagRepositoryInMemoryImpl
 */
public class SearchMetadaTagRepositoryTests extends SearchBookServiceApplicationTests {

    @Autowired
    SearchMetadaTagRepository searchMetadaTagRepository;

    String metadata = "JAX-RS";

    @Test
    public void addSearchMetadataTest(){
        SearchMetadaTag searchMetadaTag = searchMetadaTagRepository.addSearchMetadata(metadata);

        assertNotNull(searchMetadaTag);
        LOGGER.info(searchMetadaTag.toString());
        assertTrue(searchMetadaTag.getMetadata().contains(metadata));
    }

    @Test
    public void removeSearchMetadataTest(){
        SearchMetadaTag searchMetadaTag = searchMetadaTagRepository.removeSearchMetadata(metadata);

        assertNotNull(searchMetadaTag);
        LOGGER.info(searchMetadaTag.toString());
        assertTrue(!searchMetadaTag.getMetadata().contains(metadata));
    }

    @Test
    public void getSearchMetadataTest(){
        SearchMetadaTag searchMetadaTag = searchMetadaTagRepository.getSearchMetadaTag();

        assertNotNull(searchMetadaTag);
        LOGGER.info(searchMetadaTag.toString());
        assertTrue(searchMetadaTag.getMetadata().size() > 0);
    }

    @Test
    public void containsSearchMetadaTagTest(){
        assertTrue(!searchMetadaTagRepository.containsSearchMetadaTag(metadata));
    }
}
