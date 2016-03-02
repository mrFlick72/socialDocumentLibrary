package it.valeriovaudi.documentlibrary.security;

import it.valeriovaudi.documentlibrary.model.DocumentLibraryUser;
import it.valeriovaudi.documentlibrary.repository.DocumentLibraryUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

/**
 * Created by Valerio on 27/05/2015.
 */
public class DocumentLibraryUserRepositoryUserDetailsServiceImpl implements DocumentLibraryUserRepositoryUserDetailsService {

    public static final String USER_ROLE = "ROLE_USER";
    private DocumentLibraryUserRepository documentLibraryUserRepository;
    private SecurityUserFactory<DocumentLibraryUser> documentLibraryUserSecurityUserFactory;

    public void setDocumentLibraryUserSecurityUserFactory(SecurityUserFactory<DocumentLibraryUser> documentLibraryUserSecurityUserFactory) {
        this.documentLibraryUserSecurityUserFactory = documentLibraryUserSecurityUserFactory;
    }

    public void setDocumentLibraryUserRepository(DocumentLibraryUserRepository documentLibraryUserRepository) {
        this.documentLibraryUserRepository = documentLibraryUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        return Optional.ofNullable(documentLibraryUserRepository.findByUserName(userName))
                .map(documentLibraryUserSecurityUserFactory::createUser)
                .orElse(null);
    }
}
