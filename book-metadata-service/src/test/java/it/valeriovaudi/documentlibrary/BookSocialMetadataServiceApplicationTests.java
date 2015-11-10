package it.valeriovaudi.documentlibrary;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.valeriovaudi.documentlibrary.model.FeedBack;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.UriComponents;

import java.util.Locale;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = BookSocialMetadataServiceApplication.class)
@WebAppConfiguration
public abstract class BookSocialMetadataServiceApplicationTests {

    protected static final Logger LOGGER  = LoggerFactory.getLogger(BookSocialMetadataServiceApplicationTests.class);

    @Autowired
    protected WebApplicationContext wac;
    protected MockMvc mockMvc;

    @Autowired
    protected MongoTemplate mongoTemplate;

    @Autowired
    protected ObjectMapper objectMapper;

    @Before
    public void before() {/*
        objectMapper =  new ObjectMapper();
        objectMapper.registerModule(new JSR310Module());*/
        this.mockMvc = webAppContextSetup(this.wac).build();
    }

    @After
    public void clearDb(){
        mongoTemplate.dropCollection(FeedBack.class);
    }

    protected String genericGet(UriComponents uriComponents) throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(uriComponents.toUri()).
                locale(Locale.ITALIAN)).
                andExpect(status().isOk()).
                andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();

        LOGGER.info("get contentAsString: " + contentAsString);

        return contentAsString;
    }

    protected String genericGet(UriComponents uriComponents,MediaType mediaType,String requestBodyCotnent) throws Exception {
        MockHttpServletRequestBuilder content = get(uriComponents.toUri());

        if(mediaType!=null){
            content.contentType(mediaType);
        }

        if(requestBodyCotnent!=null){
            content.content(requestBodyCotnent);
        }

        MvcResult mvcResult = mockMvc.perform(content).
                andExpect(status().isOk()).
                andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        String contentAsString = response.getContentAsString();

        LOGGER.info("post location header: " + response.getHeaderValue("LOCATION"));
        LOGGER.info("post contentAsString: " + contentAsString);

        return contentAsString;
    }

    protected String genericPost(UriComponents uriComponents,int status,MediaType mediaType,String requestBodyCotnent) throws Exception {
        MockHttpServletRequestBuilder content = post(uriComponents.toUri());
        if(mediaType!=null){
            content.contentType(mediaType);
        }

        if(requestBodyCotnent!=null){
            content.content(requestBodyCotnent);
        }

        MvcResult mvcResult = mockMvc.perform(content).
                andExpect(status().is(status)).
                andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        String contentAsString = response.getContentAsString();

        LOGGER.info("post location header: " + response.getHeaderValue("LOCATION"));
        LOGGER.info("post contentAsString: " + contentAsString);

        return contentAsString;
    }

}
