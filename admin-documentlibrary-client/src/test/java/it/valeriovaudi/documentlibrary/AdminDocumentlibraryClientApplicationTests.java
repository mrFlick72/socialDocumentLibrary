package it.valeriovaudi.documentlibrary;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
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
@SpringApplicationConfiguration(classes = AdminDocumentlibraryClientApplication.class)
@WebAppConfiguration
public abstract class AdminDocumentlibraryClientApplicationTests {

	protected static final Logger LOGGER = LoggerFactory.getLogger(AdminDocumentlibraryClientApplicationTests.class);

	@Autowired
	protected WebApplicationContext wac;
	protected MockMvc mockMvc;

	protected ObjectMapper objectMapper;

	@Before
	public void before() {
		objectMapper =  new ObjectMapper();
		this.mockMvc = webAppContextSetup(this.wac).build();
	}

	protected String genericGet(UriComponents uriComponents) throws Exception {
		MvcResult mvcResult = mockMvc.perform(get(uriComponents.toUri())).
				andExpect(status().isOk()).
				andReturn();

		String contentAsString = mvcResult.getResponse().getContentAsString();

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

		String contentAsString = mvcResult.getResponse().getContentAsString();

		return contentAsString;
	}
}
