package it.valeriovaudi.documentlibrary.service;

import it.valeriovaudi.documentlibrary.config.MessagingConfig;
import it.valeriovaudi.documentlibrary.notify.service.HistoryNotifyEntryService;
import it.valeriovaudi.documentlibrary.web.model.BookMasterDTO;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.transformer.Transformer;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.jms.ConnectionFactory;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static org.springframework.http.RequestEntity.post;
import static org.springframework.web.util.UriComponentsBuilder.fromPath;

/**
 * Created by Valerio on 16/06/2015.
 */
@Configuration
public class CreateBookService {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(CreateBookService.class);

    public static final String PDF_BOOK_MASTER_AUTHOR_HEADER_KEY                          = "pdfBookMasterAuthor";
    public static final String PDF_BOOK_MASTER_DESCRIPTION_HEADER_KEY                     = "pdfBookMasterDescription";
    public static final String PDF_BOOK_MASTER_INFO_HEADER_KEY                            = "pdfBookMasterInfo";

    public static final String PDF_BOOK_MASTER_ID_HEADER_KEY                              = "pdfBookMasterId";
    public static final String PDF_BOOK_MASTER_FILE_NAME_HEADER_KEY                       = "pdfBookMasterFileName";
    public static final String BOOK_ID_HEADER_KEY                                         = "bookId";

    public static final String UPLOAD_BOOK_FORWARD_CONTENT_HEADER_ID                      = "forwardBookContentMetadata";

    @Value("${bookRepositoryService.bookServiceEndPoint.baseUrl}")
    private String bookRepositoryServicBaseUrl;

    @Value("${searchBookService.searchBookService.baseUrl}")
    private String searchBookServiceBaseUrl;

    @Autowired
    private RestTemplate bookRepositoryServiceRestTemplate;

    @Autowired
    private RestTemplate searchBookServiceRestTemplate;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private HistoryNotifyEntryService historyNotifyEntryService;

    public void setBookRepositoryServiceRestTemplate(RestTemplate bookRepositoryServiceRestTemplate) {
        this.bookRepositoryServiceRestTemplate = bookRepositoryServiceRestTemplate;
    }

    public void setSearchBookServiceRestTemplate(RestTemplate searchBookServiceRestTemplate) {
        this.searchBookServiceRestTemplate = searchBookServiceRestTemplate;
    }

    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void setBookRepositoryServicBaseUrl(String bookRepositoryServicBaseUrl) {
        this.bookRepositoryServicBaseUrl = bookRepositoryServicBaseUrl;
    }

    public void setSearchBookServiceBaseUrl(String searchBookServiceBaseUrl) {
        this.searchBookServiceBaseUrl = searchBookServiceBaseUrl;
    }

    public void setHistoryNotifyEntryService(HistoryNotifyEntryService historyNotifyEntryService) {
        this.historyNotifyEntryService = historyNotifyEntryService;
    }

    @Bean
    public MessageChannel uploadBookInChannel(){
        return new DirectChannel();
    }

    @Bean
    public IntegrationFlow uploadBookPageEndPoint(ConnectionFactory connectionFactory){
        return integrationFlowDefinition -> {integrationFlowDefinition
                .channel(uploadBookInChannel())
                .enrichHeaders(headerEnricherSpec -> headerEnricherSpec.headerExpression(UPLOAD_BOOK_FORWARD_CONTENT_HEADER_ID, "payload.metadata"))
                .<BookMasterDTO>handle((payload, headers) -> {
                    try {
                        return uploadBook(payload);
                    } catch (IOException e) {
                        LOGGER.error(e.getMessage());
                    }
                    return null;
                }, serviceActivatingHandlerGenericEndpointSpec -> serviceActivatingHandlerGenericEndpointSpec.requiresReply(true))
                .transform(transformer())
                .handleWithAdapter(adapters -> adapters.jms(connectionFactory).destination(UPLOAD_BOOK_FORWARD_CONTENT_HEADER_ID));
        };
    }


    @Bean
    public Transformer transformer() {
        return message -> {
            Map<String, Object> payload = new HashMap<>();
            payload.put(UPLOAD_BOOK_FORWARD_CONTENT_HEADER_ID,message.getHeaders().get(UPLOAD_BOOK_FORWARD_CONTENT_HEADER_ID));
            return MessageBuilder.withPayload(payload).setHeader("JMSCorrelationID", ((ResponseEntity) message.getPayload()).getBody().toString()).build();
        };
    }

    public ResponseEntity uploadBook(BookMasterDTO bookMasterDTO) throws IOException {
        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();

        Path tempFile = Files.createTempFile("", "");
        bookMasterDTO.getFile().transferTo(tempFile.toFile());

        String originalFilename = bookMasterDTO.getFile().getOriginalFilename();
        map.add("bookFile", new FileSystemResource(tempFile.toFile()));
        map.add("bookName", originalFilename.substring(0, originalFilename.length()-4));
        map.add("author", bookMasterDTO.getAuthor());
        map.add("description", bookMasterDTO.getDescription());

        String serviceUrl = String.format("%s/%s", bookRepositoryServicBaseUrl, "book");
        RequestEntity<LinkedMultiValueMap<String, Object>> requestEntity = post(fromPath(serviceUrl).build().toUri())
                                                                            .contentType(MediaType.MULTIPART_FORM_DATA)
                                                                            .body(map);

        return bookRepositoryServiceRestTemplate.exchange(serviceUrl, HttpMethod.POST, requestEntity, String.class);
    }
    /**
     *  {
     *      bookId:'...',
     *      bookName:'....',
     *      searchTags : ['...', '...', '...'],
     *      published:true/false
     *  }
     * */
    @JmsListener(destination = MessagingConfig.CREATE_BOOK_RESULT_DESTINATION_NAME)
    public void createSearchBookIndex(Map<String,Object> map){
        Map<String,String>  pdfBookMasterInfo = (Map<String, String>) map.get(PDF_BOOK_MASTER_INFO_HEADER_KEY);
        Map messageContent = (Map) jmsTemplate.receiveSelectedAndConvert(UPLOAD_BOOK_FORWARD_CONTENT_HEADER_ID, String.format("JMSCorrelationID='%s'",map.get("uuid")));

        List<String> bookMetadata = (List<String>) messageContent.get(CreateBookService.UPLOAD_BOOK_FORWARD_CONTENT_HEADER_ID);
        Optional<List<String>> searchTags = Optional.ofNullable(bookMetadata);
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

        for (String metadata : searchTags.orElse(new ArrayList<>())) {
            arrayBuilder.add(metadata);
        }

        String requestBody = Json.createObjectBuilder()
                .add("bookId", String.valueOf(map.get(BOOK_ID_HEADER_KEY)))
                .add("bookName", pdfBookMasterInfo.get(PDF_BOOK_MASTER_FILE_NAME_HEADER_KEY))
                .add("searchTags", arrayBuilder.build())
                .add("published", false)
                .build().toString();

        URI postUri = fromPath(searchBookServiceBaseUrl).build().toUri();
        RequestEntity<String> body = post(postUri).contentType(MediaType.APPLICATION_JSON).body(requestBody);
        searchBookServiceRestTemplate.exchange(searchBookServiceBaseUrl, HttpMethod.POST, body, Void.class).getStatusCode().toString();
        // send a websocket message for info that the book's processing has been started
        historyNotifyEntryService.createNotify(pdfBookMasterInfo.get(PDF_BOOK_MASTER_FILE_NAME_HEADER_KEY),String.valueOf(map.get(BOOK_ID_HEADER_KEY)));
    }
}
