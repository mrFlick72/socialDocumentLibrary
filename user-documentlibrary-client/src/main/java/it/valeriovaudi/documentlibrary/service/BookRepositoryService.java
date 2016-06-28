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
public class BookRepositoryService extends AbstractService {

    @Value("${bookRepositoryService.bookServiceEndPoint.baseUrl}")
    private String bookRepositoryService;

    @Autowired
    @LoadBalanced
    private RestTemplate bookRepositoryServiceRestTemplate;

    public void setBookRepositoryService(String bookRepositoryService) {
        this.bookRepositoryService = bookRepositoryService;
    }

    public void setBookRepositoryServiceRestTemplate(RestTemplate bookRepositoryServiceRestTemplate) {
        this.bookRepositoryServiceRestTemplate = bookRepositoryServiceRestTemplate;
    }

    @HystrixCommand(fallbackMethod = "getBookDataByIdFallbackMethod")
    public Observable<ResponseEntity<String>> getBookDataById(String bookId){
        return Observable.create(subscriber -> {
            try {
                if (!subscriber.isUnsubscribed()) {
                    URI uri = fromHttpUrl(String.format("%s/book/%s.json?startRecord=1&pageSize=1", bookRepositoryService, bookId)).build().toUri();
                    subscriber.onNext(bookRepositoryServiceRestTemplate.exchange(uri, HttpMethod.GET, RequestEntity.EMPTY, String.class));
                    subscriber.onCompleted();
                }
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });

    }

    private ResponseEntity<String> getBookDataByIdFallbackMethod(String bookId){
        return getEmptyJsonObject();
    }
}
