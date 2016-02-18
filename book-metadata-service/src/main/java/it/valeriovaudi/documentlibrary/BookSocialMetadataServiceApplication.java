package it.valeriovaudi.documentlibrary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableEurekaClient
@EnableMongoRepositories("it.valeriovaudi.documentlibrary.repository")
public class BookSocialMetadataServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookSocialMetadataServiceApplication.class, args);
    }
}
