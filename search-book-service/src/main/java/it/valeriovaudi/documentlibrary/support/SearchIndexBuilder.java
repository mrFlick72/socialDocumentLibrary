package it.valeriovaudi.documentlibrary.support;

import it.valeriovaudi.documentlibrary.model.SearchIndex;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Valerio on 14/06/2015.
 */
public class SearchIndexBuilder {

    private SearchIndex searchIndex;
    private BookNameSearchTokenizer bookNameSearchTokenizer = new BookNameSearchTokenizerImpl();

    private SearchIndexBuilder(){

    }

    public static SearchIndexBuilder newSearchIndexBuilder(){
        SearchIndex searchIndex = new SearchIndex();
        searchIndex.setBookNameTokens(new ArrayList<>());
        searchIndex.setSearchTags(new ArrayList<>());

        return newSearchIndexBuilder(searchIndex);
    }

    public static SearchIndexBuilder newSearchIndexBuilder(SearchIndex searchIndex){
        SearchIndexBuilder searchIndexBuilder = new SearchIndexBuilder();
        searchIndexBuilder.setSearchIndex(searchIndex);

        return searchIndexBuilder;
    }

    private void setSearchIndex(SearchIndex searchIndex) {
        this.searchIndex = searchIndex;
    }

    public SearchIndexBuilder bookId(String bookId){
        this.searchIndex.setBookId(bookId);
        return this;
    }

    public SearchIndexBuilder bookName(String booName){
        this.searchIndex.setBookName(booName);
        this.searchIndex.setBookNameTokens(bookNameSearchTokenizer.bookNameTokenizer(booName));

        return this;
    }

    public SearchIndexBuilder addSearchTags(String... searchTags){
        this.searchIndex.getSearchTags().addAll(Arrays.asList(searchTags));
        return this;
    }

    public SearchIndexBuilder removeSearchTags(String... searchTags){
        this.searchIndex.getSearchTags().removeAll(Arrays.asList(searchTags));
        return this;
    }


    public SearchIndexBuilder published(boolean published){
        this.searchIndex.setPublished(published);
        return this;
    }

    public SearchIndex build(){
        return this.searchIndex;
    }
}
