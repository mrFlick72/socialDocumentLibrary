package it.valeriovaudi.documentlibrary.service;

/**
 * Created by Valerio on 05/06/2015.
 */

import it.valeriovaudi.documentlibrary.SearchBookServiceApplicationTests;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.StringReader;
import java.util.List;

public class SearchMetadaTagServiceTests extends SearchBookServiceApplicationTests {

    @Test
    public void getSearchMetadaTagCreatedTest() throws Exception {
        String content = genericGet(UriComponentsBuilder.fromPath("/searchMetadaTag").build());
        Assert.assertNotNull(content);

        List searchMetadaTags = objectMapper.readValue(new StringReader(content), List.class);
        Assert.assertNotNull(searchMetadaTags);
        Assert.assertFalse(searchMetadaTags.isEmpty());
        LOGGER.info(searchMetadaTags.toString());
    }
}
