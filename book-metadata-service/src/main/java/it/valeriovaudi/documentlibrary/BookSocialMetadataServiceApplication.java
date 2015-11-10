package it.valeriovaudi.documentlibrary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories("it.valeriovaudi.documentlibrary.repository")
public class BookSocialMetadataServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookSocialMetadataServiceApplication.class, args);
    }
}
