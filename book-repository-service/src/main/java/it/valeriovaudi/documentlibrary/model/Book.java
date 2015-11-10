package it.valeriovaudi.documentlibrary.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

/**
 * Created by Valerio on 29/04/2015.
 */
@Document
public class Book {
    @Id
    private String id;

    private String name;
    private String author;
    private String description;

    private Map<Integer,Object> pageId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Map<Integer, Object> getPageId() {
        return pageId;
    }

    public void setPageId(Map<Integer, Object> pageId) {
        this.pageId = pageId;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", description='" + description + '\'' +
                ", pageId=" + pageId +
                '}';
    }
}
