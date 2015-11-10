package it.valeriovaudi.documentlibrary.repository;

import it.valeriovaudi.documentlibrary.model.DocumentLibraryUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Valerio on 26/05/2015.
 */
public interface DocumentLibraryUserRepository extends JpaRepository<DocumentLibraryUser,Long> {
    DocumentLibraryUser findByUserName(String userName);
}
