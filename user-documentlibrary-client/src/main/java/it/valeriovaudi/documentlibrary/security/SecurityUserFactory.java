package it.valeriovaudi.documentlibrary.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Created by Valerio on 26/07/2014.
 */
public interface SecurityUserFactory<T> {

    Authentication getAutenticatedUser(T t);

    UserDetails createUser(T t);

    T securityAccontWithPasswordEncoded(T t);
}
