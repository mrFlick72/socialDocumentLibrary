package it.valeriovaudi.documentlibrary.repository;

import it.valeriovaudi.documentlibrary.model.Book;
import it.valeriovaudi.documentlibrary.model.Page;

import java.util.List;

/**
 * Created by Valerio on 04/05/2015.
 */
public interface BookRepository {

    Book save(Book book,List<Page> pageList);
    List<Book> readAllBooks(int pageNumber,int pageSize);
    Book readBook(String id,int startRecord,int pageWindowSize);
    long countAllBooks();

    List<Page> read(String bookId,int pageStart,int pageEnd);
    Page read(String bookId,int pageIndex) ;
    Book addPage(String bookId,Page page);

    Book updateBook(Book book);
}
