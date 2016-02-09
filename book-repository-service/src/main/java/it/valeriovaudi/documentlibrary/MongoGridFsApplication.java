package it.valeriovaudi.documentlibrary;

import org.springframework.beans.factory.config.PlaceholderConfigurerSupport;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms
@EnableIntegration
@IntegrationComponentScan
@PropertySources(value = {
        @PropertySource("classpath:bookService.properties")
})
public class MongoGridFsApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(MongoGridFsApplication.class, args);
    }

    @Bean
    public static PlaceholderConfigurerSupport propertyPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
