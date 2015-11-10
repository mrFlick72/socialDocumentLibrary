package it.valeriovaudi.documentlibrary.model;

import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * Created by Valerio on 07/05/2015.
 */
public class PdfBookMaster implements Serializable{
    private String bookName;
    private String author;
    private String description;
    private transient MultipartFile bookFile;

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
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

    public MultipartFile getBookFile() {
        return bookFile;
    }

    public void setBookFile(MultipartFile bookFile) {
        this.bookFile = bookFile;
    }

    @Override
    public String toString() {
        return "PdfBookMaster{" +
                "bookName='" + bookName + '\'' +
                ", author='" + author + '\'' +
                ", description='" + description + '\'' +
                ", bookFile=" + bookFile +
                '}';
    }
}