package it.valeriovaudi.documentlibrary.repository;

import it.valeriovaudi.documentlibrary.model.Book;
import it.valeriovaudi.documentlibrary.model.DocumentLibraryUser;
import it.valeriovaudi.documentlibrary.model.builder.BookBuilder;
import it.valeriovaudi.documentlibrary.model.builder.DocumentLibraryUserBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;

/**
 * Created by Valerio on 27/05/2015.
 */
public abstract class BasicRepositoryTestsStep {

    private BasicRepositoryTestsStep(){

    }

    public static DocumentLibraryUser insertUserStepWithMaster(DocumentLibraryUserRepository documentLibraryUserRepository,DocumentLibraryUser userMaster){
        assertNotNull(userMaster);
        return documentLibraryUserRepository.save(userMaster);
    }

    public static DocumentLibraryUser insertUserStep(DocumentLibraryUserRepository documentLibraryUserRepository){
        DocumentLibraryUser user = DocumentLibraryUserBuilder.newDocumentLibraryUserBuilder()
                                                            .firstName("Valerio")
                                                            .lastName("Vaudi")
                                                            .userName("valval")
                                                            .password("valval")
                                                            .mail("valerio.vaudi@gmail.com")
                                                            .build();

        return insertUserStepWithMaster(documentLibraryUserRepository, user);
    }

    public static Book insertBookStep(BookRepository bookRepository){
        BookBuilder bookBuilder = BookBuilder.newBookBuilder();
        Book book = bookBuilder
                .pageBookMark(1)
                .bookId(UUID.randomUUID().toString())
                .localDate(LocalDateTime.now())
                .build();

        return insertBookStepWithMaster(bookRepository,book);
    }

    public static Book insertBookStepWithMaster(BookRepository bookRepository,Book bookMaster){
        assertNotNull(bookMaster);
        return bookRepository.save(bookMaster);
    }
}
