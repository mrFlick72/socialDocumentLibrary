package it.valeriovaudi.documentlibrary.support;

import org.springframework.data.mongodb.core.query.Query;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * Created by Valerio on 04/05/2015.
 */
public abstract class MongoDbCommonQueryFactory {

    private MongoDbCommonQueryFactory() {
    }

    public static Query createQueryFindById(Object id){
        return query(where("_id").is(id));
    }
}
