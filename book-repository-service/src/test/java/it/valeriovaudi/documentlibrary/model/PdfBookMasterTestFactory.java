package it.valeriovaudi.documentlibrary.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockMultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.lang.ClassLoader.getSystemClassLoader;

/**
 * Created by Valerio on 07/05/2015.
 */
public class PdfBookMasterTestFactory {
    protected static final Logger LOGGER = LoggerFactory.getLogger(PdfBookMasterTestFactory.class);

    public static PdfBookMaster pdfBookMaster(String pdfFileName) {
        PdfBookMaster pdfBookMaster =  new PdfBookMaster();
        try {
            URI uri = getSystemClassLoader().getResource(pdfFileName).toURI();
            Path path = Paths.get(uri);
            pdfBookMaster.setBookName(path.getFileName().toString());
            pdfBookMaster.setDescription("Description for test");
            pdfBookMaster.setAuthor("Author for test");
            pdfBookMaster.setBookFile(new MockMultipartFile("bookFile",new FileInputStream(path.toFile())));
        } catch (URISyntaxException | IOException e) {
            LOGGER.error(e.getMessage());
        }

        return pdfBookMaster;
    }
}
