package it.valeriovaudi.documentlibrary;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.gridfs.GridFSDBFile;
import it.valeriovaudi.documentlibrary.model.Book;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.MediaType;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.UriComponents;

import java.util.List;

import static it.valeriovaudi.documentlibrary.support.MongoDbCommonQueryFactory.createQueryFindById;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringApplicationConfiguration(classes = MongoGridFsApplication.class)
public abstract class MongoGridFsApplicationTests {
	protected static final Logger LOGGER = LoggerFactory.getLogger(MongoGridFsApplicationTests.class);

	@Autowired
	protected WebApplicationContext wac;
	protected MockMvc mockMvc;
	@Autowired
	protected MongoTemplate mongoTemplate;

	@Autowired
	protected GridFsTemplate gridFsTemplate;

	@Autowired
	protected JmsTemplate jmsTemplate;

	protected ObjectMapper objectMapper;

	@Before
	public void before() {
		objectMapper =  new ObjectMapper();
		this.mockMvc = webAppContextSetup(this.wac).build();
	}

	@After
	public void clearDb(){
		mongoTemplate.dropCollection(Book.class);

		List<GridFSDBFile> gridFSDBFiles = gridFsTemplate.find(Query.query(Criteria.where("")));
		for (GridFSDBFile gridFSDBFile : gridFSDBFiles) {
			gridFsTemplate.delete(createQueryFindById(gridFSDBFile.getId()));
		}

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
