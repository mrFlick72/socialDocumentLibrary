package it.valeriovaudi.documentlibrary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.ErrorPage;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
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
@EnableEurekaClient
@EnableCircuitBreaker
@EnableZuulProxy
@RibbonClients
@EnableAspectJAutoProxy(proxyTargetClass = true) // without this declaration the RestTemplate injection will be fails becouse spring cloud proxied this class for load balance with netflix ribbon
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