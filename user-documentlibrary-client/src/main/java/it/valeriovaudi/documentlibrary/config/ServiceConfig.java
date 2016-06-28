package it.valeriovaudi.documentlibrary.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.netflix.ribbon.RibbonClientHttpRequestFactory;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Valerio on 16/06/2015.
 */
@Configuration
public class ServiceConfig {

    @Bean
    @LoadBalanced
    public RestTemplate bookRepositoryServiceRestTemplate(SpringClientFactory clientFactory){
        RibbonClientHttpRequestFactory ribbonClientHttpRequestFactory = new RibbonClientHttpRequestFactory(clientFactory);
        return new RestTemplate(ribbonClientHttpRequestFactory);
    }

    @Bean
    @LoadBalanced
    public RestTemplate bookMetadataServiceRestTemplate(SpringClientFactory clientFactory){
        RibbonClientHttpRequestFactory ribbonClientHttpRequestFactory = new RibbonClientHttpRequestFactory(clientFactory);
        return new RestTemplate(ribbonClientHttpRequestFactory);
    }

    @Bean
    @LoadBalanced
    public RestTemplate searchBookServiceRestTemplate(SpringClientFactory clientFactory){
        RibbonClientHttpRequestFactory ribbonClientHttpRequestFactory = new RibbonClientHttpRequestFactory(clientFactory);
        return new RestTemplate(ribbonClientHttpRequestFactory);
    }
}
