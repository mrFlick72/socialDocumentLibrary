package it.valeriovaudi.documentlibrary.repository;

import it.valeriovaudi.documentlibrary.model.FeedBack;

import java.util.List;

/**
 * Created by Valerio on 05/06/2015.
 */
public interface FeedBackCustomOperationRepository{
    List<FeedBack> getFeedBack(String useName,String bookId);
}
