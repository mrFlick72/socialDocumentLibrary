package it.valeriovaudi.documentlibrary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.ErrorPage;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "it.valeriovaudi.documentlibrary.repository")
@EnableTransactionManagement
@PropertySource("classpath:restBaseUrl.properties")
@EnableEurekaClient
@EnableFeignClients
@RibbonClients
@EnableCircuitBreaker
@EnableZuulProxy
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

@RestController
class RestControllerApi{

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDocumentLibraryClientApplication.class);

    @Autowired
    DiscoveryClient discoveryClient;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    BookServiceInterface bookServiceInterface;

    @RequestMapping("/myService/" +
            "getService")
//    @HystrixCommand(fallbackMethod = "getServiceUnavaiable")
    public ResponseEntity getServiceUrl(){
        LOGGER.info("services: " + discoveryClient.getServices());
        LOGGER.info("services: " + bookServiceInterface.service());
        return ResponseEntity.ok("OK");
    }

    public ResponseEntity getServiceUnavaiable(){
        return ResponseEntity.ok("Service unavaiable");
    }

}

@FeignClient("book-repository-service")
@RestController
interface BookServiceInterface{

    @RequestMapping(method = RequestMethod.GET, value = "bookService//book")
    String service();
}