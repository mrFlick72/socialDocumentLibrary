package it.valeriovaudi.documentlibrary.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by Valerio on 26/05/2015.
 */

@Entity
@Table
public class Book implements Serializable  {

    private static final long SERIAL_VERSION_UID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String bookId;
    private int pageBookMark;
    private String bookName;
    // last read date
    private LocalDateTime localDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public int getPageBookMark() {
        return pageBookMark;
    }

    public void setPageBookMark(int pageBookMark) {
        this.pageBookMark = pageBookMark;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public LocalDateTime getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDateTime localDate) {
        this.localDate = localDate;
    }
}
