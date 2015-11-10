package it.valeriovaudi.documentlibrary.service;

import it.valeriovaudi.documentlibrary.BookSocialMetadataServiceApplicationTests;
import it.valeriovaudi.documentlibrary.repository.FeedBackRepository;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

import static it.valeriovaudi.documentlibrary.repository.FeedBackRepositoryTests.BOOK_ID;
import static it.valeriovaudi.documentlibrary.repository.FeedBackRepositoryTests.USER_NAME;

/**
 * Created by Valerio on 05/06/2015.
 */

public class FeedBackServiceFailureTests extends BookSocialMetadataServiceApplicationTests {

    @Autowired
    FeedBackRepository feedBackRepository;

    @Autowired
    FeedBackService feedBackService;

    @Test
    public void addFeedBackCreatedTest() throws Exception {
        UriComponents addFeedBackTestUriComponents = UriComponentsBuilder.fromPath("/feedBack").buildAndExpand(USER_NAME, BOOK_ID);
        genericPost(addFeedBackTestUriComponents, HttpStatus.BAD_REQUEST.value(), MediaType.APPLICATION_JSON, "{}");
        genericPost(addFeedBackTestUriComponents, HttpStatus.BAD_REQUEST.value(), MediaType.APPLICATION_JSON, "");
        genericPost(addFeedBackTestUriComponents, HttpStatus.BAD_REQUEST.value(), MediaType.APPLICATION_JSON, null);
    }

    // with user Name and bookId
    @Test
    public void getFeedBackWithFiltringTest() throws Exception {
        UriComponents getFeedBackTestUriComponents = UriComponentsBuilder.fromPath("/feedBack/{userName}/{bookId}/data")
                                                                        .queryParam("filterQuery","score")
                                                                        .buildAndExpand("1", "1");
        String result = genericGet(getFeedBackTestUriComponents);

        Assert.assertEquals(result, "{}");
    }


    @Test
    public void getFeedBackWithoutFiltringTest() throws Exception {
        UriComponents getFeedBackTestUriComponents = UriComponentsBuilder.fromPath("/feedBack/{userName}/{bookId}/data")
                .buildAndExpand("1", "1");
        String result = genericGet(getFeedBackTestUriComponents);

        Assert.assertNotNull(result);
        Assert.assertNotEquals("",result);
        Assert.assertEquals(result, "{}");
    }



    @Test
    public void getFeedBackBatchWithoutFiltringTest() throws Exception {
        Random random = new Random();

        List<Map<String,String>> mapList = new ArrayList<>();

        Map<String,String> stringStringMap = new HashMap<>();

        for(int j = 0 ; j < 10 ; j++) {
            stringStringMap.put("userName", String.valueOf(random.nextInt()));
            stringStringMap.put("bookId", String.valueOf(random.nextInt()));

            mapList.add(stringStringMap);
        }

        UriComponents getFeedBackTestUriComponents = UriComponentsBuilder.fromPath("/feedBack/batch").build();
        String result = genericGet(getFeedBackTestUriComponents,MediaType.APPLICATION_JSON,objectMapper.writeValueAsString(mapList));
        Assert.assertEquals(result, "[]");
    }

}
