package it.valeriovaudi.documentlibrary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

@SpringBootApplication
@PropertySource("classpath:restBaseUrl.properties")
@EnableJms
@EnableIntegration
@EnableWebSocketMessageBroker
@EnableJpaRepositories(basePackages = "it.valeriovaudi.documentlibrary.notify.repository")
@EnableTransactionManagement
public class AdminDocumentlibraryClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminDocumentlibraryClientApplication.class, args);
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer placeholderConfigurerSupport() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public EmbeddedServletContainerCustomizer exceptionHandling() {
        return container -> container.addErrorPages(new ErrorPage("/exception"));
    }
}