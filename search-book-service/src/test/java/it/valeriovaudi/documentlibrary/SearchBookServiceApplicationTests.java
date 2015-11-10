package it.valeriovaudi.documentlibrary;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.valeriovaudi.documentlibrary.model.SearchIndex;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SearchBookServiceApplication.class)
@WebAppConfiguration
public abstract class SearchBookServiceApplicationTests {

	protected static final Logger LOGGER  = LoggerFactory.getLogger(SearchBookServiceApplicationTests.class);

	@Autowired
	protected WebApplicationContext wac;
	protected MockMvc mockMvc;

	@Autowired
	protected MongoTemplate mongoTemplate;

	@Autowired
	protected ObjectMapper objectMapper;

	protected boolean postProcessTestMethods = true;

	@Before
	public void before() {
		this.mockMvc = webAppContextSetup(this.wac).build();
	}

	@After
	public void clearDb(){
		if(postProcessTestMethods){
			mongoTemplate.dropCollection(SearchIndex.class);
		}
	}

	protected String genericGet(UriComponents uriComponents) throws Exception {
		MvcResult mvcResult = mockMvc.perform(get(uriComponents.toUri())).
				andExpect(status().isOk()).
				andReturn();

		return mvcResult.getResponse().getContentAsString();
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

		LOGGER.info("post location header: " + response.getHeaderValue("LOCATION"));

		return response.getContentAsString();
	}
}
