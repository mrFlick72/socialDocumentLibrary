package it.valeriovaudi.documentlibrary.model;

import it.valeriovaudi.documentlibrary.validator.ValidFile;
import it.valeriovaudi.validator.FileAllowedContentType;
import it.valeriovaudi.validator.FileAllowedExtensions;
import it.valeriovaudi.validator.FileNotNull;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * Created by Valerio on 07/05/2015.
 */

@ValidFile(allowedContentType = "application/pdf")
public class PdfBookMaster implements Serializable{
    private String bookName;
    private String author;
    private String description;

    @NotEmpty
    private transient String contentType;

    @FileNotNull
    @FileAllowedContentType(allowedContentType = {"application/pdf","application/octet-stream"})
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

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public MultipartFile getBookFile() {
        return bookFile;
    }

    public void setBookFile(MultipartFile bookFile) {
        this.bookFile = bookFile;
    }
}
