package it.valeriovaudi.documentlibrary.builder;

import it.valeriovaudi.documentlibrary.model.Book;
import it.valeriovaudi.documentlibrary.model.Page;

import java.util.HashMap;

import static it.valeriovaudi.documentlibrary.support.PageSupport.fileNameToindex;

/**
 * Created by Valerio on 30/04/2015.
 */
public class BookBuilder {
    private Book book;

    private BookBuilder(){
        this.book = new Book();
        this.book.setPageId(new HashMap<>());
    }

    private void setBook(Book book){
        this.book = book;
    }

    public static BookBuilder newBookBuilder(){
        return new BookBuilder();
    }

    public static BookBuilder newBookBuilder(Book book){
        BookBuilder bookBuilder = new BookBuilder();

        if(book.getPageId()==null){
            book.setPageId(new HashMap<>());
        }

        bookBuilder.setBook(book);

        return bookBuilder;
    }

    public BookBuilder id(String id){
        this.book.setId(id);
        return this;
    }

    public BookBuilder name(String name){
        this.book.setName(name);
        return this;
    }

    public BookBuilder author(String author){
        this.book.setAuthor(author);
        return this;
    }

    public BookBuilder description(String description){
        this.book.setDescription(description);
        return this;
    }

    public BookBuilder addPage(Integer pageNamber, String pageId){
        this.book.getPageId().put(pageNamber,pageId);
        return this;
    }

    public BookBuilder addPage(Page page){
        assert page!=null;
        this.book.getPageId().put(fileNameToindex(page),page.getId());
        return this;
    }

    public Book build(){
        return book;
    }
}
