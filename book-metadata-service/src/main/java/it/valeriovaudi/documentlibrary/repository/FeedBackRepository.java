package it.valeriovaudi.documentlibrary.repository;

import it.valeriovaudi.documentlibrary.model.FeedBack;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by Valerio on 06/06/2015.
 */
public interface FeedBackRepository extends MongoRepository<FeedBack,String>,FeedBackCustomOperationRepository {
}
