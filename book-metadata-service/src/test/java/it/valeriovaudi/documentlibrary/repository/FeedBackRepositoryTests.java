package it.valeriovaudi.documentlibrary.repository;

import it.valeriovaudi.documentlibrary.BookSocialMetadataServiceApplicationTests;
import it.valeriovaudi.documentlibrary.model.FeedBack;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Valerio on 06/06/2015.
 */
public class FeedBackRepositoryTests extends BookSocialMetadataServiceApplicationTests {

    @Autowired
    FeedBackRepository feedBackRepository;

    List<String> currentFeadBackId = new ArrayList<>();

    public static final String BOOK_ID= UUID.randomUUID().toString();
    public static final String USER_NAME = "valval";

    int docuemtnInstance = 5;

    @Before
    public void setUpMongo(){
        for(int i = 0 ; i < docuemtnInstance ;i++){
            FeedBack save = feedBackRepository.save(createFeedBack(USER_NAME, BOOK_ID));
            currentFeadBackId.add(save.getId());

            LOGGER.info(save.toString());
        }
    }

    @After
    public void clearMongo(){
        for (String ids : currentFeadBackId) {
            feedBackRepository.delete(ids);
        }
    }


    @Test
    public void getFeedBackTest(){
        LOGGER.info(String.valueOf(currentFeadBackId));

        List<FeedBack> feedBack = feedBackRepository.getFeedBack(USER_NAME, BOOK_ID);
        Assert.assertNotNull(feedBack);
        LOGGER.info(feedBack.toString());

    }

    public static FeedBack createFeedBack(String USER_NAME, String BOOK_ID){
        FeedBack feedBack = new FeedBack();

        feedBack.setBookId(BOOK_ID);
        feedBack.setUserName(USER_NAME);

        feedBack.setDateTime(LocalDateTime.now());
        feedBack.setFeedbackTitle("titolo di prova");
        feedBack.setFeedbackBody("body di un commento");
        feedBack.setScore(150);

        return feedBack;
    }
}
