package it.valeriovaudi.documentlibrary.service;

import it.valeriovaudi.documentlibrary.BookSocialMetadataServiceApplicationTests;
import it.valeriovaudi.documentlibrary.model.FeedBack;
import it.valeriovaudi.documentlibrary.repository.FeedBackRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

import static it.valeriovaudi.documentlibrary.repository.FeedBackRepositoryTests.*;
import static it.valeriovaudi.documentlibrary.service.FeedBackService.BOOK_ID_KEY;
import static it.valeriovaudi.documentlibrary.service.FeedBackService.USER_NAME_KEY;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Valerio on 05/06/2015.
 */

public class FeedBackServiceTests extends BookSocialMetadataServiceApplicationTests {

    @Autowired
    FeedBackRepository feedBackRepository;

    @Autowired
    FeedBackService feedBackService;

    @Test
    public void addFeedBackCreatedTest() throws Exception {
        FeedBack feedBack = createFeedBack(USER_NAME, BOOK_ID);
        UriComponents addFeedBackTestUriComponents = UriComponentsBuilder.fromPath("/feedBack").build();
        genericPost(addFeedBackTestUriComponents, HttpStatus.CREATED.value(), MediaType.APPLICATION_JSON, objectMapper.writeValueAsString(feedBack));

        List<FeedBack> feedBackAux = feedBackRepository.getFeedBack(USER_NAME, BOOK_ID);
        assertNotNull(feedBackAux);
        assertEquals(1, feedBackAux.size());

        LOGGER.info(feedBackAux.toString());
    }

    @Test
    public void updateFeedBackNotModifiedTest() throws Exception {
        FeedBack feedBack = createFeedBack(USER_NAME, BOOK_ID);
        FeedBack save = feedBackRepository.save(feedBack);

        feedBack.setFeadbackBody("new FeedBack body");

        UriComponents updateFeedBackTestUriComponents = UriComponentsBuilder.fromPath("/feedBack/{feedBackId}").buildAndExpand(save.getId());
        mockMvc.perform(put(updateFeedBackTestUriComponents.toUri())
                        .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(feedBack)))
                .andExpect(status().isNoContent());


        List<FeedBack> feedBackAux = feedBackRepository.getFeedBack(USER_NAME, BOOK_ID);
        assertNotNull(feedBackAux);
        assertEquals(1, feedBackAux.size());
        assertEquals(feedBackAux.get(0).getFeadbackBody(), feedBack.getFeadbackBody());

        LOGGER.info(feedBackAux.toString());
    }


    // with user Name and bookId
    @Test
    public void getFeedBackWithFiltringTest() throws Exception {
        FeedBack feedBack = createFeedBack(USER_NAME, BOOK_ID);
        FeedBack save = feedBackRepository.save(feedBack);

        UriComponents getFeedBackTestUriComponents = UriComponentsBuilder.fromPath("/feedBack/{userName}/{bookId}/data")
                                                                        .queryParam("filterQuery","score")
                                                                        .buildAndExpand(save.getUserName(), save.getBookId());
        String result = genericGet(getFeedBackTestUriComponents);

        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("score", feedBack.getScore());

        assertEquals(objectMapper.writeValueAsString(resultMap), result);
    }


    @Test
    public void getFeedBackWithoutFiltringTest() throws Exception {
        FeedBack feedBack = createFeedBack(USER_NAME, BOOK_ID);
        FeedBack save = feedBackRepository.save(feedBack);

        UriComponents getFeedBackTestUriComponents = UriComponentsBuilder.fromPath("/feedBack/{userName}/{bookId}/data")
                .buildAndExpand(save.getUserName(), save.getBookId());
        String result = genericGet(getFeedBackTestUriComponents);

        LOGGER.info("result: " + result);

        assertNotNull(result);
        assertNotEquals("", result);
        assertEquals(objectMapper.writeValueAsString(feedBack), result);
    }

    // with only UserName
    @Test
    public void getFeedBackByUserNameWithFiltringTest() throws Exception {
        FeedBack feedBack = createFeedBack(USER_NAME, BOOK_ID);
        FeedBack save = feedBackRepository.save(feedBack);

        UriComponents getFeedBackTestUriComponents = UriComponentsBuilder.fromPath("/feedBack/userName/{userName}/data")
                .queryParam("filterQuery", "score")
                .buildAndExpand(save.getUserName());
        String result = genericGet(getFeedBackTestUriComponents);

        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("score", feedBack.getScore());

        assertEquals(objectMapper.writeValueAsString(Arrays.asList(resultMap)), result);
    }

    @Test
    public void getFeedBackByUserNameWithoutFiltringTest() throws Exception {
        FeedBack feedBack = createFeedBack(USER_NAME, BOOK_ID);
        FeedBack save = feedBackRepository.save(feedBack);

        UriComponents getFeedBackTestUriComponents = UriComponentsBuilder.fromPath("/feedBack/userName/{userName}/data")
                .buildAndExpand(save.getUserName());
        String result = genericGet(getFeedBackTestUriComponents);

        assertNotNull(result);
        assertNotEquals("", result);
        assertEquals(objectMapper.writeValueAsString(Arrays.asList(feedBack)), result);
    }


    // with only BookId
    @Test
    public void getFeedBackByBookIdWithFiltringTest() throws Exception {
        FeedBack feedBack = createFeedBack(USER_NAME, BOOK_ID);
        FeedBack save = feedBackRepository.save(feedBack);

        UriComponents getFeedBackTestUriComponents = UriComponentsBuilder.fromPath("/feedBack/bookId/{bookId}/data")
                .queryParam("filterQuery", "score")
                .buildAndExpand(save.getBookId());
        String result = genericGet(getFeedBackTestUriComponents);

        List<Map<String,Object>> scores = new ArrayList<>();
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("score",feedBack.getScore());
        scores.add(resultMap);

        assertEquals(objectMapper.writeValueAsString(scores), result);
    }


    @Test
    public void getFeedBackByBookIdWithoutFiltringTest() throws Exception {
        FeedBack feedBack = createFeedBack(USER_NAME, BOOK_ID);
        FeedBack save = feedBackRepository.save(feedBack);

        UriComponents getFeedBackTestUriComponents = UriComponentsBuilder.fromPath("/feedBack/bookId/{bookId}/data")
                .buildAndExpand(save.getBookId());
        String result = genericGet(getFeedBackTestUriComponents);

        assertNotNull(result);
        assertNotEquals("", result);
        assertEquals(objectMapper.writeValueAsString(Arrays.asList(feedBack)), result);
    }


    @Test
    public void getFeedBackBatchWithoutFiltringTest() throws Exception {
        FeedBack save;
        Random random = new Random();

        List<Map<String,String>> mapList = new ArrayList<>();
        Map<String,String> stringStringMap;

        List<FeedBack> expectedFeedBackResult = new ArrayList<>();

        for(int j = 0 ; j < 10 ; j++) {
            save = feedBackRepository.save(createFeedBack(USER_NAME, String.valueOf(random.nextLong())));
            stringStringMap = new HashMap<>();
            stringStringMap.put("userName", save.getUserName());
            stringStringMap.put("bookId", save.getBookId());

            expectedFeedBackResult.add(save);
            mapList.add(stringStringMap);
        }

        UriComponents getFeedBackTestUriComponents = UriComponentsBuilder.fromPath("/feedBack/batch").build();
        String result = genericGet(getFeedBackTestUriComponents,MediaType.APPLICATION_JSON,objectMapper.writeValueAsString(mapList));
        assertEquals(objectMapper.writeValueAsString(expectedFeedBackResult), result);
    }

    @Test
    public void getFeedBackBatchWithFiltringTest() throws Exception {
        FeedBack save;
        Random random = new Random();

        List<Map<String,String>> mapList = new ArrayList<>();
        Map<String,String> stringStringMap;

        Map<String,Object> resultChekerMap;
        List<Map<String,Object>> resultChekerMapList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            save = feedBackRepository.save(createFeedBack(USER_NAME, String.valueOf(random.nextLong())));

            stringStringMap = new HashMap<>();
            stringStringMap.put(USER_NAME_KEY, save.getUserName());
            stringStringMap.put(BOOK_ID_KEY, save.getBookId());

            mapList.add(stringStringMap);

            resultChekerMap = new HashMap<>();
            resultChekerMap.put("score", save.getScore());
            resultChekerMap.put("userName", USER_NAME);

            resultChekerMapList.add(resultChekerMap);
        }

        UriComponents getFeedBackTestUriComponents = UriComponentsBuilder.fromPath("/feedBack/batch").queryParam("filterQuery","score","userName").build();
        String result = genericGet(getFeedBackTestUriComponents, MediaType.APPLICATION_JSON, objectMapper.writeValueAsString(resultChekerMapList));
        assertEquals(objectMapper.writeValueAsString(resultChekerMapList), result);
    }
}
