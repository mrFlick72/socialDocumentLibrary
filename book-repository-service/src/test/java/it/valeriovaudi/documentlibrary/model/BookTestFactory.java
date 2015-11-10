package it.valeriovaudi.documentlibrary.model;

import it.valeriovaudi.documentlibrary.builder.BookBuilder;
import it.valeriovaudi.documentlibrary.builder.PageBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static java.lang.ClassLoader.getSystemClassLoader;

/**
 * Created by Valerio on 30/04/2015.
 */
public class BookTestFactory {
    protected static final Logger LOGGER = LoggerFactory.getLogger(BookTestFactory.class);
    public static final String testBook = "SocialDocumentLibrary";
    public static final String testBookWithExtensions = "SocialDocumentLibrary.pdf";

    public static Book createBook() {
        Book newBook = BookBuilder.newBookBuilder()
                .author("Valerio Vaudi")
                .name(testBook)
                .description("description")
                .build();

        return newBook;
    }


    public static List<Page> createPages(){
        List<Page> pageList = new ArrayList<>();
        URI uri = null;
        try {
            uri = getSystemClassLoader().getResource(testBook).toURI();
        } catch (URISyntaxException e) {
            LOGGER.error(e.getMessage());
        }
        Path path = Paths.get(uri);
        try {
            Files.walk(path).forEach(path1 -> {
                try {
                    pageList.add(PageBuilder.newPageBuilder()
                            .bytes(new FileInputStream(path1.toFile()))
                            .fileName(path1.getFileName().toString()).build());
                } catch (Exception e) {
                    LOGGER.error(e.getMessage());
                }
            });
        } catch (IOException e){
            LOGGER.error(e.getMessage());
        }

        return pageList;
    }
}
