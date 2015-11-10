package it.valeriovaudi.documentlibrary.security;

import it.valeriovaudi.documentlibrary.model.DocumentLibraryUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Created by Valerio on 27/05/2015.
 */
public class SecurityUserFactoryImpl implements SecurityUserFactory<DocumentLibraryUser> {

    @Override
    public Authentication getAutenticatedUser(DocumentLibraryUser documentLibraryUser) {
        return new UsernamePasswordAuthenticationToken(createUser(documentLibraryUser), documentLibraryUser.getPassword() , AuthorityUtils.createAuthorityList(DocumentLibraryUserRepositoryUserDetailsServiceImpl.USER_ROLE));
    }

    @Override
    public UserDetails createUser(DocumentLibraryUser documentLibraryUser) {
        return new User(documentLibraryUser.getUserName(), documentLibraryUser.getPassword(), AuthorityUtils.createAuthorityList(DocumentLibraryUserRepositoryUserDetailsServiceImpl.USER_ROLE));
    }

    @Override
    public DocumentLibraryUser securityAccontWithPasswordEncoded(DocumentLibraryUser documentLibraryUser) {
        throw new UnsupportedOperationException("this version of security implementation don't support the password encryption");
    }

}
