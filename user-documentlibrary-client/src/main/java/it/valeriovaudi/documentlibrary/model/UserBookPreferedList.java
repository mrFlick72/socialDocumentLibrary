package it.valeriovaudi.documentlibrary.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Valerio on 26/05/2015.
 */
@Entity
@Table
@NamedQueries(value = {
        @NamedQuery(name = "UserBookPreferedList.findByUserName",query = "select userBookPreferedList from UserBookPreferedList userBookPreferedList where userBookPreferedList.documentLibraryUser.userName=:userNameParam")
})
public class UserBookPreferedList implements Serializable{

    private static final long SERIAL_VERSION_UID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @OneToOne
    private DocumentLibraryUser documentLibraryUser;

    @OneToMany
    @JoinTable(name = "BOOK_READ_LIST_ASSOCIATION_TABLE",
               joinColumns = {@JoinColumn(name = "PREFERRED_LIST_ID")},
               inverseJoinColumns = {@JoinColumn(name = "BOOK_ID")})
    private List<Book> booksReadList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DocumentLibraryUser getDocumentLibraryUser() {
        return documentLibraryUser;
    }

    public void setDocumentLibraryUser(DocumentLibraryUser documentLibraryUser) {
        this.documentLibraryUser = documentLibraryUser;
    }

    public List<Book> getBooksReadList() {
        return booksReadList;
    }

    public void setBooksReadList(List<Book> booksReadList) {
        this.booksReadList = booksReadList;
    }

    @Override
    public String toString() {
        return "UserBookPreferredList{" +
                "id=" + id +
                ", documentLibraryUser=" + documentLibraryUser +
                ", booksReadList=" + booksReadList +
                '}';
    }
}
