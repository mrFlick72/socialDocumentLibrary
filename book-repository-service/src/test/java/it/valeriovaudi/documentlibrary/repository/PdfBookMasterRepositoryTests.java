package it.valeriovaudi.documentlibrary.repository;

import it.valeriovaudi.documentlibrary.MongoGridFsApplicationTests;
import it.valeriovaudi.documentlibrary.model.BookTestFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static java.lang.ClassLoader.getSystemClassLoader;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Valerio on 04/05/2015.
 */
public class PdfBookMasterRepositoryTests extends MongoGridFsApplicationTests {
    String currentId;

    @Autowired
    PdfBookMasterRepository pdfMasterRepository;

    @Test
    public void savePdfMasterTest() throws FileNotFoundException {
        URI uri = null;
        try {
            uri = getSystemClassLoader().getResource(BookTestFactory.testBookWithExtensions).toURI();
        } catch (URISyntaxException e) {
            LOGGER.error(e.getMessage());
        }
        Path path = Paths.get(uri);

        Map<String,String> map = new HashMap<>();
        map.put("description","Description");
        map.put("author","Valerio");

        String pdfId = pdfMasterRepository.savePdfMaster(new FileInputStream(path.toFile()), path.getFileName().toString(), map);
        LOGGER.info("pdfId: " + pdfId);
        assertNotNull(pdfId);

        currentId = pdfId;
    }

    @Test
    public void readPdfMasterTest() throws IOException {
        savePdfMasterTest();

        byte[] bytes = pdfMasterRepository.readPdfMaster(currentId);
        LOGGER.info("bytes: " + bytes);
        assertNotNull(bytes);
    }

    @Test
    public void deletePdfMasterTest() throws FileNotFoundException {
        savePdfMasterTest();

        boolean deletePdfMaster = pdfMasterRepository.deletePdfMaster(currentId);
        LOGGER.info("deletePdfMaster: " + deletePdfMaster);
        assertTrue(deletePdfMaster);
    }
}
