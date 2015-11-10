package it.valeriovaudi.documentlibrary.repository;

import it.valeriovaudi.documentlibrary.UserDocumentLibraryClientApplicationAbstractTests;
import it.valeriovaudi.documentlibrary.model.Book;
import it.valeriovaudi.documentlibrary.model.DocumentLibraryUser;
import it.valeriovaudi.documentlibrary.model.UserBookPreferedList;
import it.valeriovaudi.documentlibrary.model.builder.UserBookPreferedListBuilder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertNotNull;

/**
 * Created by Valerio on 27/05/2015.
 */
public class UserBookPreferredListRepositoryTests extends UserDocumentLibraryClientApplicationAbstractTests {

    @Autowired
    DocumentLibraryUserRepository documentLibraryUserRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    UserBookPreferedListRepository userBookPreferedListRepository;

    @Test
    public void correctInsertTest(){
        DocumentLibraryUser documentLibraryUser = BasicRepositoryTestsStep.insertUserStep(documentLibraryUserRepository);
        assertNotNull(documentLibraryUser);

        Book book = BasicRepositoryTestsStep.insertBookStep(bookRepository);
        assertNotNull(book);

        UserBookPreferedList userBookPreferredList = UserBookPreferedListBuilder.newUserBookPreferedListBuilder()
                .documentLibraryUser(documentLibraryUser)
                .addBook(book)
                .build();

        UserBookPreferedList save = userBookPreferedListRepository.save(userBookPreferredList);
        LOGGER.info(save.toString());
    }
}
