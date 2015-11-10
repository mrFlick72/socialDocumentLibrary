package it.valeriovaudi.documentlibrary.model.builder;

import it.valeriovaudi.documentlibrary.model.Book;
import it.valeriovaudi.documentlibrary.model.DocumentLibraryUser;
import it.valeriovaudi.documentlibrary.model.UserBookPreferedList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Valerio on 27/05/2015.
 */
public class UserBookPreferedListBuilder {

    private UserBookPreferedList userBookPreferedList;

    private UserBookPreferedListBuilder() {
    }

    public void setUserBookPreferedList(UserBookPreferedList userBookPreferedList) {
        this.userBookPreferedList = userBookPreferedList;
    }

    public static UserBookPreferedListBuilder newUserBookPreferedListBuilder(){
        UserBookPreferedList userBookPreferedList = new UserBookPreferedList();
        userBookPreferedList.setBooksReadList(new ArrayList<>());
        return newUserBookPreferedListBuilder(userBookPreferedList);
    }

    public static UserBookPreferedListBuilder newUserBookPreferedListBuilder(UserBookPreferedList userBookPreferedList){
        UserBookPreferedListBuilder userBookPreferedListBuilder = new UserBookPreferedListBuilder();
        userBookPreferedListBuilder.setUserBookPreferedList(userBookPreferedList);
        return userBookPreferedListBuilder;
    }

    public UserBookPreferedListBuilder id(Long id){
        this.userBookPreferedList.setId(id);
        return this;
    }

    public UserBookPreferedListBuilder documentLibraryUser(DocumentLibraryUser documentLibraryUser){
        this.userBookPreferedList.setDocumentLibraryUser(documentLibraryUser);
        return this;
    }

    public UserBookPreferedListBuilder booksReadList(List<Book> bookList){
        this.userBookPreferedList.setBooksReadList(bookList);
        return this;
    }

    public UserBookPreferedListBuilder addBook(Book book){
        this.userBookPreferedList.getBooksReadList().add(book);
        return this;
    }

    public UserBookPreferedListBuilder removeBook(Book book){
        this.userBookPreferedList.getBooksReadList().remove(book);
        return this;
    }

    public UserBookPreferedList build(){
        return userBookPreferedList;
    }
}
