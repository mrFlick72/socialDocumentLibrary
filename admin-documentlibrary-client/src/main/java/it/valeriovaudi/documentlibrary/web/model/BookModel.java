package it.valeriovaudi.documentlibrary.web.model;

import java.util.List;

/**
 * Created by Valerio on 18/06/2015.
 */
public class BookModel {

    private String bookId;
    private String name;
    private String author;
    private String description;
    private List<String> metadata;
    private boolean published;
    private boolean delete;

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getMetadata() {
        return metadata;
    }

    public void setMetadata(List<String> metadata) {
        this.metadata = metadata;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    @Override
    public String toString() {
        return "BookModel{" +
                "bookId='" + bookId + '\'' +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", description='" + description + '\'' +
                ", metadata=" + metadata +
                ", published=" + published +
                ", delete=" + delete +
                '}';
    }
}
