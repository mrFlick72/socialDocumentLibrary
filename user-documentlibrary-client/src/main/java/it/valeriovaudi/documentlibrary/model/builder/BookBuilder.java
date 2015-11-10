package it.valeriovaudi.documentlibrary.model.builder;

import it.valeriovaudi.documentlibrary.model.Book;

import java.time.LocalDateTime;

/**
 * Created by Valerio on 27/05/2015.
 */
public class BookBuilder {

    private Book book;

    private BookBuilder() {
    }

    private void setBook(Book book) {
        this.book = book;
    }

    public static BookBuilder newBookBuilder(){
        BookBuilder bookBuilder = new BookBuilder();
        bookBuilder.setBook(new Book());
        return bookBuilder;
    }

    public static BookBuilder newBookBuilder(Book book){
        BookBuilder bookBuilder = new BookBuilder();
        bookBuilder.setBook(book);
        return bookBuilder;
    }


    public BookBuilder id(long id) {
        this.book.setId(id);
        return this;
    }

    public BookBuilder bookName(String bookName) {
        this.book.setBookName(bookName);
        return this;
    }

    public BookBuilder bookId(String bookId){
        this.book.setBookId(bookId);
        return this;
    }

    public BookBuilder pageBookMark(int pageBookMark){
        this.book.setPageBookMark(pageBookMark);
        return this;
    }

    public BookBuilder localDate(LocalDateTime localDate) {
        this.book.setLocalDate(localDate);
        return this;
    }

    public Book build(){
        return book;
    }
}
