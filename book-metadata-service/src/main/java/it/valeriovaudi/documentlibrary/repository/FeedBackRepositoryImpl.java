package it.valeriovaudi.documentlibrary.repository;

import it.valeriovaudi.documentlibrary.model.FeedBack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * Created by Valerio on 06/06/2015.
 *
 * the calss name is required by the standerd policy of spring data
 */
public class FeedBackRepositoryImpl implements FeedBackCustomOperationRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<FeedBack> getFeedBack(String userName, String bookId) {
        Criteria criteria = Optional.ofNullable(userName).map(userNameAux -> where("userName").is(userNameAux)).orElse(null);
        if(bookId !=null){
            criteria = criteria == null ? where("bookId").is(bookId) : criteria.and("bookId").is(bookId);
        }
        return  Optional.ofNullable(criteria).map(criteriaAux -> mongoTemplate.find(Query.query(criteriaAux),FeedBack.class)).orElse(new ArrayList<>());
    }
}
