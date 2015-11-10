package it.valeriovaudi.documentlibrary.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Created by Valerio on 10/06/2015.
 */
@Document(collection = "SearchIndexCollection")
public class SearchIndex {

    @Id
    private String bookId;

    private String bookName;

    @JsonIgnore
    private List<String> bookNameTokens;

    private List<String> searchTags;
    private boolean published;

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public List<String> getBookNameTokens() {
        return bookNameTokens;
    }

    public void setBookNameTokens(List<String> bookNameTokens) {
        this.bookNameTokens = bookNameTokens;
    }

    public List<String> getSearchTags() {
        return searchTags;
    }

    public void setSearchTags(List<String> searchTags) {
        this.searchTags = searchTags;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    @Override
    public String toString() {
        return "SearchIndex{" +
                "bookId='" + bookId + '\'' +
                ", bookName='" + bookName + '\'' +
                ", bookNameTokens=" + bookNameTokens +
                ", searchTags=" + searchTags +
                ", published=" + published +
                '}';
    }
}
