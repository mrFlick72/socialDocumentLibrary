package it.valeriovaudi.documentlibrary.config;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Valerio on 16/06/2015.
 */
@Configuration
@Profile("standAlone")
public class ServiceConfig {

    @Bean
    public RestTemplate bookRepositoryServiceRestTemplate(SpringClientFactory springClientFactory, LoadBalancerClient loadBalancerClient){
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(100);
        cm.closeExpiredConnections();

        CloseableHttpClient httpclient = HttpClients.custom().setConnectionManager(cm).build();

        HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpComponentsClientHttpRequestFactory.setHttpClient(httpclient);

        return new RestTemplate(httpComponentsClientHttpRequestFactory);
    }

    @Bean
    public RestTemplate bookMetadataServiceRestTemplate(SpringClientFactory springClientFactory, LoadBalancerClient loadBalancerClient){
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(100);
        cm.closeExpiredConnections();

        CloseableHttpClient httpclient = HttpClients.custom().setConnectionManager(cm).build();

        HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpComponentsClientHttpRequestFactory.setHttpClient(httpclient);

        return new RestTemplate(httpComponentsClientHttpRequestFactory);
    }

    @Bean
    public RestTemplate searchBookServiceRestTemplate(SpringClientFactory springClientFactory, LoadBalancerClient loadBalancerClient){
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(100);
        cm.closeExpiredConnections();

        CloseableHttpClient httpclient = HttpClients.custom().setConnectionManager(cm).build();

        HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpComponentsClientHttpRequestFactory.setHttpClient(httpclient);

        return new RestTemplate(httpComponentsClientHttpRequestFactory);
    }
}
