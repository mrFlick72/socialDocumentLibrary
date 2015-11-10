package it.valeriovaudi.documentlibrary.repository;

import it.valeriovaudi.documentlibrary.model.SearchIndex;
import it.valeriovaudi.documentlibrary.support.BookNameSearchTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * Created by Valerio on 11/06/2015.
 */
@Repository
public class SearchIndexRepositoryMongoImpl implements SearchIndexRepository {

    public static final int NO_PAGINATION_PARAMITER = -1;
    public static final String NO_PAGINATION_STRING_PARAMITER = "-1";

    public static final int EMPTY_PAGE_PARAMITER= 0;
    public static final String EMPTY_PAGE_STRING_PARAMITER = "0";

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private BookNameSearchTokenizer bookNameSearchTokenizer;

    public void setBookNameSearchTokenizer(BookNameSearchTokenizer bookNameSearchTokenizer) {
        this.bookNameSearchTokenizer = bookNameSearchTokenizer;
    }

    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public SearchIndex saveIndex(SearchIndex searchIndex) {
        searchIndex.setBookNameTokens(bookNameSearchTokenizer.bookNameTokenizer(searchIndex.getBookName()));
        mongoTemplate.save(searchIndex);

        return searchIndex;
    }

    @Override
    public SearchIndex publishSearchBookIndex(String bookId, boolean published) {
        SearchIndex searckIndexById = findSearchIndexById(bookId);
        searckIndexById.setPublished(published);
        mongoTemplate.save(searckIndexById);
        return searckIndexById;
    }

    @Override
    public SearchIndex addSearchTags(String bookId, String... searchTags) {
        return searchIndexUpdate(bookId, (SearchIndex searchIndex) -> searchIndex.getSearchTags().addAll(Arrays.asList(searchTags)));
    }

    @Override
    public SearchIndex removeSearchTags(String bookId, String... searchTags) {
        return searchIndexUpdate(bookId, (SearchIndex searchIndex) -> searchIndex.getSearchTags().removeAll(Arrays.asList(searchTags)));
    }

    @Override
    public SearchIndex findSearchIndexById(String bookId) {
        return mongoTemplate.findOne(Query.query(where("_id").is(bookId)), SearchIndex.class);
    }

    @Override
    public List<SearchIndex> findSearchIndexByMetadata(String bookName, String... searchTags) {
        List<SearchIndex> searchIndexes;
        Criteria criteria = new Criteria();
        List<Criteria> criteriaList = new ArrayList<>();
        if(bookName!=null){
            List<String> bookNameTokens = bookNameSearchTokenizer.bookNameTokenizer(bookName);
            Criteria bookNameCriteria = where("bookNameTokens").in(bookNameTokens.toArray(new String[bookNameTokens.size()]));
            criteriaList.add(bookNameCriteria);
        }

        if(searchTags!=null){
            Criteria searchTagsCriteria = where("searchTags").in(searchTags);
            criteriaList.add(searchTagsCriteria);
        }

        criteria.orOperator(criteriaList.toArray(new Criteria[criteriaList.size()]));
        criteria.and("published").is(true);

        searchIndexes =  mongoTemplate.find(Query.query(criteria), SearchIndex.class);

        return searchIndexes;
    }

    @Override
    public List<SearchIndex> findAllSearchIndex(int startPage, int pageSize) {
        if(pageSize!=0){
            boolean selectQueryCriteria = startPage == NO_PAGINATION_PARAMITER || pageSize == NO_PAGINATION_PARAMITER;
            Query query = selectQueryCriteria ? Query.query(Criteria.where("")) : Query.query(Criteria.where("")).limit(pageSize).skip(startPage * pageSize);
            return mongoTemplate.find(query, SearchIndex.class);
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public boolean deleteSearchIndex(String bookId) {
        mongoTemplate.findAndRemove(Query.query(where("_id").is(bookId).and("published").in(false)), SearchIndex.class);
        return findSearchIndexById(bookId) == null;
    }

    private SearchIndex searchIndexUpdate(String bookId,Consumer<SearchIndex> update){
        SearchIndex searckIndexById = findSearchIndexById(bookId);
        update.accept(searckIndexById);
        mongoTemplate.save(searckIndexById);
        return searckIndexById;
    }
}
