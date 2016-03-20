package it.valeriovaudi.documentlibrary.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.command.ObservableResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import rx.Observable;
import rx.Subscriber;

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
    public Observable<String> getBookDataById(String bookId){
        return new ObservableResult<String>() {
            @Override
            public String invoke() {
                return bookRepositoryServiceRestTemplate.exchange(fromHttpUrl(String.format("%s/book/%s.json?startRecord=1&pageSize=1", bookRepositoryService, bookId)).build().toUri(), HttpMethod.GET, RequestEntity.EMPTY, String.class).getBody();
            }
        };
    }

    private String getBookDataByIdFallbackMethod(String bookId){
        log.error("bookId: " + bookId);
        log.error("Fail");
         new ObservableResult<String>() {
            @Override
            public String invoke() {
                return getEmptyJsonObject().getBody();
            }
        };

        return getEmptyJsonObject().getBody();
    }
}
