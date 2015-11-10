package it.valeriovaudi.documentlibrary.notify.model;

import org.springframework.boot.orm.jpa.EntityScan;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Valerio on 16/09/2015.
 */
@Entity
@Table
@NamedQueries(value = {
        @NamedQuery(name = "HistoryNotifyEntry.deleteByBookId",query = "DELETE FROM HistoryNotifyEntry entity WHERE entity.bookId  =:bookIdAux"),
        @NamedQuery(name = "HistoryNotifyEntry.findByBookId", query = "SELECT entity FROM HistoryNotifyEntry entity WHERE entity.bookId  =:bookIdAux")
})
public class HistoryNotifyEntry implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String bookName;
    private String bookId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }
}
