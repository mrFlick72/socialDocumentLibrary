package it.valeriovaudi.documentlibrary.factory;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.valeriovaudi.documentlibrary.UserDocumentLibraryClientApplicationAbstractTests;
import it.valeriovaudi.documentlibrary.model.factory.UiJsonFactory;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Valerio on 26/02/2016.
 */
public class UserFeedBackFactoryFromJsonTests {
    protected static final Logger LOGGER = LoggerFactory.getLogger(UserDocumentLibraryClientApplicationAbstractTests.class);

    @Test
    public void testGetUiJsonFromService() throws JsonProcessingException {
        Map<String,String> expeted = new HashMap<>();
        expeted.put("firstName", "Valerio12");

        Map build = UiJsonFactory.newUiJsonFactory()
                .putProperty("nome", "Valerio")
                .trasformPropertyKey("nome", "firstName")
                .putProperty("firstName", "Valerio1")
                .putProperty("firstName", "Valerio12")
                .putProperty("firstName", null).build();

        LOGGER.info(build.toString());
        Assert.assertEquals(expeted,build);
    }
}
