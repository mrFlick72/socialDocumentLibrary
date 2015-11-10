package it.valeriovaudi.documentlibrary.web.model;

import it.valeriovaudi.documentlibrary.validator.FileAllowedExtensions;
import it.valeriovaudi.documentlibrary.validator.FileNotNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Created by Valerio on 23/05/2015.
 */
public class BookMasterDTO {

    @FileNotNull
    @FileAllowedExtensions(allowedExtensions = {"pdf"})
    private MultipartFile file;
    private List<String> metadata;
    private String description;
    private String author;

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public List<String> getMetadata() {
        return metadata;
    }

    public void setMetadata(List<String> metadata) {
        this.metadata = metadata;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "BookMasterDTO{" +
                "file=" + file +
                ", metadata=" + metadata +
                ", description='" + description + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
}