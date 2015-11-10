package it.valeriovaudi.documentlibrary.repository;


import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;
import it.valeriovaudi.documentlibrary.builder.BookBuilder;
import it.valeriovaudi.documentlibrary.builder.PageBuilder;
import it.valeriovaudi.documentlibrary.model.Book;
import it.valeriovaudi.documentlibrary.model.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static it.valeriovaudi.documentlibrary.support.GridFSDBFileSupport.gridFSDBFile2ByteArray;
import static it.valeriovaudi.documentlibrary.support.MongoDbCommonQueryFactory.createQueryFindById;
import static it.valeriovaudi.documentlibrary.support.PageSupport.fileNameToindex;

/**
 * Created by Valerio on 29/04/2015.
 */
@Repository
public class BookRepositoryMongoImpl implements BookRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(BookRepositoryMongoImpl.class);
    public static final Integer INVALID_PAGINATION_PARAMITER = -1;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public void setGridFsTemplate(GridFsTemplate gridFsTemplate) {
        this.gridFsTemplate = gridFsTemplate;
    }

    @Override
    public Book save(Book book,List<Page> pageList) {
        assert book!=null;

        GridFSFile store;
        BookBuilder bookBuilder = BookBuilder.newBookBuilder(book);
        if(pageList != null){
            for (Page page : pageList) {
                store = gridFsTemplate.store(new ByteArrayInputStream(page.getBytes()), page.getFileName());
                bookBuilder.addPage(fileNameToindex(page), String.valueOf(store.getId()));
            }
        }
        mongoTemplate.save(bookBuilder.build());

        return book;
    }

    @Override
    public List<Book> readAllBooks(int pageNumber, int pageSize) {
        Query skip = Query.query(Criteria.where("")).limit(pageSize).skip(pageNumber * pageSize);
        List<Book> books = mongoTemplate.find(skip, Book.class);
        List<Book> bookList = new ArrayList<>();
        for (Book book : books) {
            bookList.add(bookPageIdPaginator(book,0,0));
        }

        return bookList;
    }

    @Override
    public Book readBook(String id,int startRecord,int pageWindowSize) {
        Book book = mongoTemplate.findById(id, Book.class);
        return bookPageIdPaginator(book, startRecord, pageWindowSize);
    }

    @Override
    public long countAllBooks() {
        return mongoTemplate.count(new Query(), "book");
    }

    @Override
    public List<Page> read(String bookId,int pageStart,int pageEnd) {
        assert bookId!=null;
        Book byId = mongoTemplate.findById(bookId, Book.class);
        return byId!=null ? readPageSet(byId.getPageId(), pageStart, pageEnd) : new ArrayList<>();
    }

    @Override
    public Page read(String bookId,int pageIndex) {
        assert bookId != null;
        Book byId = mongoTemplate.findById(bookId, Book.class);
        return readPageSet(byId.getPageId(), pageIndex, pageIndex).get(0);
    }

    @Override
    public Book addPage(String bookId, Page page) {
        Book readBook = readBook(bookId,INVALID_PAGINATION_PARAMITER,INVALID_PAGINATION_PARAMITER);
        GridFSFile store = gridFsTemplate.store(new ByteArrayInputStream(page.getBytes()), page.getFileName());
        Book book = BookBuilder.newBookBuilder(readBook).addPage(fileNameToindex(page), String.valueOf(store.getId())).build();

        mongoTemplate.save(book);
        return book;
    }

    @Override
    public Book updateBook(Book book) {
        Assert.notNull(book.getId());
        mongoTemplate.save(book);
        return book;
    }

    private List<Page> readPageSet(Map<Integer, Object> pageId,int pageStart,int pageEnd) {
        List<Page> pages = new ArrayList<>();

        GridFSDBFile gridFSDBFile;
        for (Map.Entry<Integer, Object> integerStringEntry : pageId.entrySet()) {
            if(integerStringEntry.getKey() >= pageStart && integerStringEntry.getKey() <= pageEnd){
                gridFSDBFile = gridFsTemplate.findOne(createQueryFindById(integerStringEntry.getValue()));
                pages.add(PageBuilder.newPageBuilder()
                        .bytes(gridFSDBFile2ByteArray(gridFSDBFile))
                        .fileName(gridFSDBFile.getFilename())
                        .id(gridFSDBFile.getId().toString())
                        .build());
            }
        }

        return pages;
    }

    private Book bookPageIdPaginator(Book book,int startRecord,int pageWindowSize){
        if(startRecord!=INVALID_PAGINATION_PARAMITER && pageWindowSize!=INVALID_PAGINATION_PARAMITER){
            Map<Integer, Object> pageIdAux = new HashMap<>();
            Map<Integer, Object> pageId = book.getPageId();

            for(int i = startRecord ; i  < startRecord + pageWindowSize ; i++){
                pageIdAux.put(i,pageId.get(i));
            }
            book.setPageId(pageIdAux);
        }

        return book;
    }

}
