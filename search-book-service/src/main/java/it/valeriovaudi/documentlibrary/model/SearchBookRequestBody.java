package it.valeriovaudi.documentlibrary.model;

import java.util.List;

/**
 * Created by Valerio on 14/06/2015.
 */
public class SearchBookRequestBody {
    private String bookName;
    private List<String> searchTags;

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public List<String> getSearchTags() {
        return searchTags;
    }

    public void setSearchTags(List<String> searchTags) {
        this.searchTags = searchTags;
    }

    @Override
    public String toString() {
        return "SearchBookRequestBody{" +
                "bookName='" + bookName + '\'' +
                ", searchTags=" + searchTags +
                '}';
    }
}
