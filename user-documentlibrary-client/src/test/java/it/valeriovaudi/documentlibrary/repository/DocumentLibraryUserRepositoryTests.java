package it.valeriovaudi.documentlibrary.repository;

import it.valeriovaudi.documentlibrary.UserDocumentLibraryClientApplicationAbstractTests;
import it.valeriovaudi.documentlibrary.model.DocumentLibraryUser;
import it.valeriovaudi.documentlibrary.model.builder.DocumentLibraryUserBuilder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

/**
 * Created by Valerio on 27/05/2015.
 */
public class DocumentLibraryUserRepositoryTests extends UserDocumentLibraryClientApplicationAbstractTests {

    @Autowired
    DocumentLibraryUserRepository documentLibraryUserRepository;

    @Test
    public void findByUserNameTest(){
        DocumentLibraryUser user = BasicRepositoryTestsStep.insertUserStep(documentLibraryUserRepository);
        assertNotNull(user);

        DocumentLibraryUser userByUserName = documentLibraryUserRepository.findByUserName(user.getUserName());
        assertNotNull(userByUserName);
        assertEquals(user.getId(),userByUserName.getId());
        assertEquals(user.getFirstName(),userByUserName.getFirstName());
        assertEquals(user.getLastName(),userByUserName.getLastName());
        assertEquals(user.getMail(),userByUserName.getMail());
        assertEquals(user.getUserName(),userByUserName.getUserName());
        assertEquals(user.getPassword(),userByUserName.getPassword());
    }

    @Test
    public void findByUserNameFailsTest(){
        DocumentLibraryUser user = BasicRepositoryTestsStep.insertUserStep(documentLibraryUserRepository);
        assertNotNull(user);

        DocumentLibraryUser userByUserName = documentLibraryUserRepository.findByUserName("valval1");
        assertNull(userByUserName);
    }


    @Test
    public void findByUserNameFailsWrongUserTest(){
        DocumentLibraryUser user = BasicRepositoryTestsStep.insertUserStep(documentLibraryUserRepository);
        assertNotNull(user);

        DocumentLibraryUserBuilder documentLibraryUserBuilder2 =
                DocumentLibraryUserBuilder.newDocumentLibraryUserBuilder();

        DocumentLibraryUser user2 = documentLibraryUserBuilder2
                .firstName("Valerio2")
                .lastName("Vaudi2")
                .userName("valval2")
                .password("valval2")
                .mail("valerio.vaudi2@gmail.com")
                .build();

        DocumentLibraryUser save2 = BasicRepositoryTestsStep.insertUserStepWithMaster(documentLibraryUserRepository, user2);
        assertNotNull(save2);

        DocumentLibraryUser userByUserName = documentLibraryUserRepository.findByUserName(user2.getUserName());
        assertNotNull(userByUserName);

        assertNotEquals(user.getId(), userByUserName.getId());
        assertNotEquals(user.getFirstName(), userByUserName.getFirstName());
        assertNotEquals(user.getLastName(), userByUserName.getLastName());
        assertNotEquals(user.getMail(), userByUserName.getMail());
        assertNotEquals(user.getUserName(), userByUserName.getUserName());
        assertNotEquals(user.getPassword(), userByUserName.getPassword());
    }
}
