package it.valeriovaudi.documentlibrary;

import org.apache.commons.logging.LogFactory;
import org.apache.tomcat.util.http.fileupload.FileUploadBase;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.MultipartProperties;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.ErrorPage;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.embedded.MultipartConfigFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.multipart.support.MultipartFilter;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

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
    public static PropertySourcesPlaceholderConfigurer placeholderConfigurerSupport(){
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public EmbeddedServletContainerCustomizer exceptionHandling() {
        return container -> container.addErrorPages(new ErrorPage("/exception"));
    }

}