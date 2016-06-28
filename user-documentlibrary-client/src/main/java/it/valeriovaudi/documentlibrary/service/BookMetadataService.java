package it.valeriovaudi.documentlibrary.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import rx.Observable;
import rx.Subscriber;

import java.net.URI;

import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;

/**
 * Created by Valerio on 19/03/2016.
 */

@Service
public class BookMetadataService extends AbstractService {

    @Value("${bookSocialMetadataService.feedBackService.baseUrl}")
    private String bookSocialMetadataBaseUrl;

    @Autowired
    @LoadBalanced
    private RestTemplate bookMetadataServiceRestTemplate;

    public void setBookSocialMetadataBaseUrl(String bookSocialMetadataBaseUrl) {
        this.bookSocialMetadataBaseUrl = bookSocialMetadataBaseUrl;
    }

    public void setBookMetadataServiceRestTemplate(RestTemplate bookMetadataServiceRestTemplate) {
        this.bookMetadataServiceRestTemplate = bookMetadataServiceRestTemplate;
    }

    @HystrixCommand(fallbackMethod = "getSocialMetadataByBookIdFallbackMethod")
    public Observable<ResponseEntity<String>> getSocialMetadataByBookId(String bookId){
        return Observable.create(subscriber -> {
            try {
                if (!subscriber.isUnsubscribed()) {
                    URI uri = fromHttpUrl(String.format("%s/bookId/%s/data", bookSocialMetadataBaseUrl, bookId)).build().toUri();
                    subscriber.onNext( bookMetadataServiceRestTemplate.getForEntity(uri, String.class));
                    subscriber.onCompleted();
                }
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }

    private ResponseEntity<String> getSocialMetadataByBookIdFallbackMethod(String bookId){
        return getEmptyJsonArray();
    }
}
