package it.valeriovaudi.documentlibrary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.ErrorPage;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
@Configuration
@EnableEurekaClient
@RibbonClients
@EnableCircuitBreaker
@EnableZuulProxy
@EnableJpaRepositories(basePackages = "it.valeriovaudi.documentlibrary.repository")
@EnableTransactionManagement
@EnableRedisHttpSession
@PropertySource("classpath:restBaseUrl.properties")
@EnableAspectJAutoProxy(proxyTargetClass = true) // without this declaration the RestTemplate injection wil be fails becouse spring cloud proxied this class for load balance with netflix ribbon
public class UserDocumentLibraryClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserDocumentLibraryClientApplication.class, args);
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer placeholderConfigurerSupport() {
        return new PropertySourcesPlaceholderConfigurer();
    }

 /*   @Bean
    public EmbeddedServletContainerCustomizer exceptionHandling() {
        return container -> container.addErrorPages(new ErrorPage("/exception"));
    }*/

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**");
            }
        };
    }
}
