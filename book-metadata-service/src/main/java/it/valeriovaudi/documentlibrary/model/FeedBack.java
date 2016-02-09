package it.valeriovaudi.documentlibrary.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Created by Valerio on 05/06/2015.
 */
@Document(collection = "FeedBackCollection")
public class FeedBack {

    @Id
    private String id;

    @NotNull
    @NotBlank
    private String bookId;

    @NotNull
    @NotBlank
    private String userName;
    private int score;

    private String feadbackTitle;
    private String feadbackBody;

    @JsonSerialize(using = com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer.class)
    @JsonDeserialize(using = com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer.class)
    private LocalDateTime dateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getFeadbackTitle() {
        return feadbackTitle;
    }

    public void setFeadbackTitle(String feadbackTitle) {
        this.feadbackTitle = feadbackTitle;
    }

    public String getFeadbackBody() {
        return feadbackBody;
    }

    public void setFeadbackBody(String feadbackBody) {
        this.feadbackBody = feadbackBody;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "FeedBack{" +
                "id='" + id + '\'' +
                ", bookId='" + bookId + '\'' +
                ", userName='" + userName + '\'' +
                ", score=" + score +
                ", feadbackTitle='" + feadbackTitle + '\'' +
                ", feadbackBody='" + feadbackBody + '\'' +
                ", dateTime=" + dateTime +
                '}';
    }
}