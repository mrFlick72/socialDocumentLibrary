package it.valeriovaudi.documentlibrary.repository;

import it.valeriovaudi.documentlibrary.model.SearchIndex;

import java.util.List;

/**
 * Created by Valerio on 09/06/2015.
 */
public interface SearchIndexRepository {

    /* insert methods */
    SearchIndex saveIndex(SearchIndex searchIndex);

    /* update methods */
    SearchIndex publishSearchBookIndex(String bookId, boolean published);

    SearchIndex addSearchTags(String bookId, String...searchTags);
    SearchIndex removeSearchTags(String bookId, String...searchTags);

    /* read methods*/
    SearchIndex findSearchIndexById(String bookId);
    List<SearchIndex> findSearchIndexByMetadata(String bookName,String... searchTags);
    List<SearchIndex> findAllSearchIndex(int startPage, int pageSize);

    boolean deleteSearchIndex(String bookId);
}
