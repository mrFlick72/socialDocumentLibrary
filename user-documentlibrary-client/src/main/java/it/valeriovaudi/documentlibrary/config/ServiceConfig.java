package it.valeriovaudi.documentlibrary.config;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Valerio on 16/06/2015.
 */
@Configuration
public class ServiceConfig {

    @Bean
    public RestTemplate bookRepositoryServiceRestTemplate(){
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(100);
        cm.closeExpiredConnections();

        CloseableHttpClient httpclient = HttpClients.custom().setConnectionManager(cm).build();

        HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpComponentsClientHttpRequestFactory.setHttpClient(httpclient);

        return new RestTemplate(httpComponentsClientHttpRequestFactory);
    }

    @Bean
    public RestTemplate bookMetadataServiceRestTemplate(){
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(100);
        cm.closeExpiredConnections();

        CloseableHttpClient httpclient = HttpClients.custom().setConnectionManager(cm).build();

        HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpComponentsClientHttpRequestFactory.setHttpClient(httpclient);

        return new RestTemplate(httpComponentsClientHttpRequestFactory);
    }

    @Bean
    public RestTemplate searchBookServiceRestTemplate(){
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(100);
        cm.closeExpiredConnections();

        CloseableHttpClient httpclient = HttpClients.custom().setConnectionManager(cm).build();

        HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpComponentsClientHttpRequestFactory.setHttpClient(httpclient);

        return new RestTemplate(httpComponentsClientHttpRequestFactory);
    }
}
