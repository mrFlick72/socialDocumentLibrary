package it.valeriovaudi.documentlibrary.repository;

import it.valeriovaudi.documentlibrary.model.FeedBack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.List;

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
        Criteria criteria = null;
        List<FeedBack> result = new ArrayList<>();
        if(userName != null){
            criteria = where("userName").is(userName);
        }

        if(bookId !=null){
            if(criteria==null){
                criteria = where("bookId").is(bookId);
            } else {
                criteria = criteria.and("bookId").is(bookId);
            }
        }
        if(criteria!=null){
            result = mongoTemplate.find(Query.query(criteria),FeedBack.class);
        }

        return result;
    }
}
