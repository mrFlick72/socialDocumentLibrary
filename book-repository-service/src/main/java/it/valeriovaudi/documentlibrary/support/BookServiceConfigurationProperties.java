package it.valeriovaudi.documentlibrary.support;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * Created by valerio on 06/07/16.
 */

@Data
@ConfigurationProperties(prefix = "bookService")
public class BookServiceConfigurationProperties  {
    private String tempFilePathBaseDir= "/opt/socialDocumentLibrary/book-repository-service/repository/temp";
    private String bookPageFileFormat="jpeg";
    private String bookPageNameFormat="page%s.%s";

    @PostConstruct
    public void init() throws IOException {
        Path path = Paths.get(tempFilePathBaseDir);
        if(!Files.exists(path)){
            Files.createDirectories(path);
        }
    }
}
