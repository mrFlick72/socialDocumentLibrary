package it.valeriovaudi.documentlibrary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.ErrorPage;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "it.valeriovaudi.documentlibrary.repository")
@EnableTransactionManagement
@PropertySource("classpath:restBaseUrl.properties")
@ServletComponentScan(basePackages = "it.valeriovaudi.documentlibrary")
@EnableEurekaClient
@EnableAspectJAutoProxy(proxyTargetClass = true) // without this declaration the RestTemplate injection wil be fails becouse spring cloud proxied this class for load balance with netflix ribbon
public class UserDocumentLibraryClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserDocumentLibraryClientApplication.class, args);
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