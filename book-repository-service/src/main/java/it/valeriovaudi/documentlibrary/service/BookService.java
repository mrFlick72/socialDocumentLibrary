package it.valeriovaudi.documentlibrary.service;

import it.valeriovaudi.documentlibrary.builder.BookBuilder;
import it.valeriovaudi.documentlibrary.builder.PageBuilder;
import it.valeriovaudi.documentlibrary.exception.FileInteractionException;
import it.valeriovaudi.documentlibrary.exception.PdfInteractionAndManipulation;
import it.valeriovaudi.documentlibrary.model.Book;
import it.valeriovaudi.documentlibrary.model.Page;
import it.valeriovaudi.documentlibrary.model.PdfBookMaster;
import it.valeriovaudi.documentlibrary.repository.BookRepository;
import it.valeriovaudi.documentlibrary.repository.PdfBookMasterRepository;
import it.valeriovaudi.documentlibrary.support.PageSupport;
import org.apache.pdfbox.io.RandomAccessBuffer;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.jms.Jms;
import org.springframework.messaging.support.MessageBuilder;

import javax.imageio.ImageIO;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

/**
 * Created by Valerio on 06/05/2015.
 */

@Configuration
public class BookService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BookService.class);

    public static final String PDF_BOOK_MASTER_AUTHOR_HEADER_KEY                          = "pdfBookMasterAuthor";
    public static final String PDF_BOOK_MASTER_DESCRIPTION_HEADER_KEY                     = "pdfBookMasterDescription";
    public static final String PDF_BOOK_MASTER_INFO_HEADER_KEY                            = "pdfBookMasterInfo";

    public static final String PDF_BOOK_MASTER_ID_HEADER_KEY                              = "pdfBookMasterId";
    public static final String PDF_BOOK_MASTER_FILE_NAME_HEADER_KEY                       = "pdfBookMasterFileName";
    public static final String BOOK_ID_HEADER_KEY                                         = "bookId";
    public static final String TEMP_FILE_PATH_HEADER_KEY                                  = "tempFilePath";

    public static final String MAP_MESSAGE_PAYLOAD_KEY                                    = "payload";
    public static final String MAP_MESSAGE_UUID_KEY                                       = "uuid";

    @Autowired
    private Environment environment;

    @Value("${bookService.bookPageFileFormat}")
    private String bookPageFileFormat;

    @Value("${bookService.bookPageNameFormat}")
    private String bookPageNameFormat;

    public void setBookPageFileFormat(String bookPageFileFormat) {
        this.bookPageFileFormat = bookPageFileFormat;
    }

    public void setBookPageNameFormat(String bookPageNameFormat) {
        this.bookPageNameFormat = bookPageNameFormat;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public DirectChannel createBookByPdfInputChannel(){
        return new DirectChannel();
    }

    @Bean
    public PublishSubscribeChannel storeBookPageByPageErrorChannel(){
        return new PublishSubscribeChannel();
    }

    @Bean
    public IntegrationFlow createBookByPdf(ConnectionFactory connectionFactory,
                                           PdfBookMasterRepository pdfBookMasterRepository,
                                           @Qualifier("createBookQueue") Destination createBookQueue){
               return  flow -> flow.channel("createBookByPdfInputChannel")
                                    .enrichHeaders(headerEnricherSpec -> headerEnricherSpec.headerExpression(MAP_MESSAGE_UUID_KEY,"payload.uuid"))
                                    .<Map<String,Object>>transform("payload.payload")
                                    .<PdfBookMaster>handle((payload, headers) -> {
                                        Map<String, Object> map = null;
                                        String pdfBookMastrId = null;
                                        try {
                                            Map<String, String> pdfMasterMetadata = new HashMap<>();
                                            pdfMasterMetadata.put(PDF_BOOK_MASTER_AUTHOR_HEADER_KEY, payload.getAuthor());
                                            pdfMasterMetadata.put(PDF_BOOK_MASTER_DESCRIPTION_HEADER_KEY, payload.getDescription());

                                            pdfBookMastrId = pdfBookMasterRepository
                                                    .savePdfMaster(payload.getBookFile().getInputStream(),
                                                            payload.getBookName(),
                                                            pdfMasterMetadata);
                                            map = new HashMap<>();
                                            map.put("pdfBookMasterId", pdfBookMastrId);
                                            map.put("pdfBookMasterFileName", payload.getBookName());
                                            map.put(PDF_BOOK_MASTER_AUTHOR_HEADER_KEY, payload.getAuthor());
                                            map.put(PDF_BOOK_MASTER_DESCRIPTION_HEADER_KEY, payload.getDescription());

                                        } catch (IOException ioe) {
                                            LOGGER.error(ioe.getMessage());
                                            return new FileInteractionException(ioe);
                                        }
                                        return map;
                                    }, serviceActivatingHandlerGenericEndpointSpec -> serviceActivatingHandlerGenericEndpointSpec.requiresReply(true))
                .handleWithAdapter(adapters -> adapters.jms(connectionFactory).destination(createBookQueue));
    }

    @Bean
    public IntegrationFlow storeBookPageByPage(ConnectionFactory connectionFactory,
                                               @Qualifier("createBookQueue") Destination createBookQueue,
                                               @Qualifier("createBookResultQueue") Destination createBookResultQueue,
                                               PdfBookMasterRepository pdfBookMasterRepository,
                                               BookRepository bookRepository){
        String tempFilePathBaseDir = environment.getProperty("bookService.storeBookPageByPage.tempFilePathBaseDir");

        return IntegrationFlows.from(Jms.messageDriverChannelAdapter(connectionFactory)
                .destination(createBookQueue)
                .errorChannel(storeBookPageByPageErrorChannel()))
                .channel(channels -> channels.executor(Executors.newScheduledThreadPool(5)))
                .enrichHeaders(headerEnricherSpec -> headerEnricherSpec.headerExpression(PDF_BOOK_MASTER_INFO_HEADER_KEY, "payload")
                                                                        .header("errorChannel","storeBookPageByPageErrorChannel"))
                .<Map<String, Object>, Book>transform(map -> {
                    BookBuilder bookBuilder = BookBuilder.newBookBuilder();
                    if (map.get(PDF_BOOK_MASTER_FILE_NAME_HEADER_KEY) != null) {
                        bookBuilder.name(String.valueOf(map.get(PDF_BOOK_MASTER_FILE_NAME_HEADER_KEY)))
                                .description(String.valueOf(map.get(PDF_BOOK_MASTER_DESCRIPTION_HEADER_KEY)))
                                .author(String.valueOf(map.get(PDF_BOOK_MASTER_AUTHOR_HEADER_KEY)));
                    }
                    return bookBuilder.build();
                })
                .<Book>handle((book, map) -> bookRepository.save(book, new ArrayList<>()),
                        serviceActivatingHandlerGenericEndpointSpec -> serviceActivatingHandlerGenericEndpointSpec.requiresReply(true))
                .enrichHeaders(headerEnricherSpec1 -> headerEnricherSpec1.headerExpression(BOOK_ID_HEADER_KEY, "payload.id"))
                .<Book>handle((payload, headers) -> {
                    Map<String, Object> pdfBookMasterInfoHeader;
                    pdfBookMasterInfoHeader = (Map<String, Object>) headers.get(PDF_BOOK_MASTER_INFO_HEADER_KEY);
                    byte[] bytes = pdfBookMasterRepository.readPdfMaster(String.valueOf(pdfBookMasterInfoHeader.get(PDF_BOOK_MASTER_ID_HEADER_KEY)));
                    Path tempFile = null;
                    Path path = Paths.get(tempFilePathBaseDir);
                    try {
                        tempFile = Files.createTempFile(path, String.valueOf(pdfBookMasterInfoHeader.get(PDF_BOOK_MASTER_ID_HEADER_KEY)), ".pdf");
                        Files.copy(new ByteArrayInputStream(bytes), tempFile, StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException ioe) {
                        LOGGER.error("ERRORE nella creazione del file temporaneo");
                        return new FileInteractionException(ioe);
                    }
                    return tempFile;
                }, serviceActivatingHandlerGenericEndpointSpec1 -> serviceActivatingHandlerGenericEndpointSpec1.requiresReply(true))
                .enrichHeaders(headerEnricherSpec2 -> headerEnricherSpec2.headerExpression(TEMP_FILE_PATH_HEADER_KEY, "payload"))
                .<Path, List<byte[]>>transform(source -> {
                    List<byte[]> result = new ArrayList<>();
                    List<PDPage> allPages = new ArrayList<>();
                    try {
                        PDDocument load = PDDocument.load(source.toFile(), new RandomAccessBuffer());
                        allPages = load.getDocumentCatalog().getAllPages();

                        for (PDPage page : allPages) {
                            try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
                                ImageIO.write(page.convertToImage(), bookPageFileFormat, byteArrayOutputStream);
                                result.add(byteArrayOutputStream.toByteArray());
                            }
                        }
                    } catch (IOException e) {
                        LOGGER.error(e.getMessage());
                        throw new PdfInteractionAndManipulation(e);
                    }

                    return result;
                })
                .split()
                .<byte[]>handle((payload2, headers2) -> {
                    String bookId = String.valueOf(headers2.get(BOOK_ID_HEADER_KEY));
                    Page page = PageBuilder
                            .newPageBuilder()
                            .fileName(PageSupport.fileName4Page(bookPageNameFormat, Integer.parseInt(String.valueOf(headers2.get("sequenceNumber"))), bookPageFileFormat))
                            .bytes(payload2)
                            .build();

                    bookRepository.addPage(bookId, page);
                    return page;
                }, serviceActivatingHandlerGenericEndpointSpec2 -> serviceActivatingHandlerGenericEndpointSpec2.requiresReply(true))
                .aggregate()
                .handle((payload1, headers1) -> {
                    Path tempFilePath = (Path) headers1.get(TEMP_FILE_PATH_HEADER_KEY);
                    LOGGER.info("tempFilePath delete job: " + tempFilePath.toFile().delete());
                    return payload1;
                })
                .handle((payload3, headers3) -> {
                    HashMap<String, Object> stringObjectHashMap = new HashMap<>();
                    stringObjectHashMap.put(PDF_BOOK_MASTER_INFO_HEADER_KEY, headers3.get(PDF_BOOK_MASTER_INFO_HEADER_KEY));
                    stringObjectHashMap.put(BOOK_ID_HEADER_KEY, headers3.get(BOOK_ID_HEADER_KEY));
                    stringObjectHashMap.put(MAP_MESSAGE_UUID_KEY,headers3.get(MAP_MESSAGE_UUID_KEY));

                    Map header = new HashMap();
                    header.putAll(headers3);
                    header.put("JMSCorrelationID",headers3.get(MAP_MESSAGE_UUID_KEY));
                    return MessageBuilder.withPayload(stringObjectHashMap).copyHeaders(header).build();
                })
                .handleWithAdapter(adapters -> adapters.jms(connectionFactory).destination(createBookResultQueue))
                .get();

    }

    @Bean
    public IntegrationFlow storeBookPageByPageErrorChannelHandler(PublishSubscribeChannel storeBookPageByPageErrorChannel,
                                                                  ConnectionFactory connectionFactory,
                                                                  @Qualifier("createBookResultQueue") Destination createBookResultQueue){
        return integrationFlowDefinition -> integrationFlowDefinition.channel(storeBookPageByPageErrorChannel)
                .handleWithAdapter(adapters -> adapters.jms(connectionFactory).destination(createBookResultQueue));

    }
 }
