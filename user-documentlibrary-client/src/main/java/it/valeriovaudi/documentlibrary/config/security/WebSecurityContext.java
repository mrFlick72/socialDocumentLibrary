package it.valeriovaudi.documentlibrary.config.security;

import it.valeriovaudi.documentlibrary.model.DocumentLibraryUser;
import it.valeriovaudi.documentlibrary.repository.DocumentLibraryUserRepository;
import it.valeriovaudi.documentlibrary.security.DocumentLibraryUserRepositoryUserDetailsServiceImpl;
import it.valeriovaudi.documentlibrary.security.SecurityUserFactory;
import it.valeriovaudi.documentlibrary.security.SecurityUserFactoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.session.SessionManagementFilter;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled = true)
public class WebSecurityContext extends WebSecurityConfigurerAdapter {
    protected Logger log = LoggerFactory.getLogger(getClass());

    /*******************************************Log-In properties******************************************************/
    private static final String LOG_IN_URL_PAGE = "/signin";
    public static final String DEFAULT_TARGET_URL_PAGE = "/bookUserList";
    private static final String AUTHENTICATION_FAILURE_URL_PAGE = "/signin?error=1";

    /*******************************************Log-Out properties*****************************************************/
    private static final String LOG_OUT_URL_PAGE = "/logout";
    private static final String LOG_OUT_SUCCESS_URL = "/signin";


    @Bean
    public DocumentLibraryUserRepositoryUserDetailsServiceImpl documentLibraryUserRepositoryUserDetailsService(DocumentLibraryUserRepository documentLibraryUserRepository){
        DocumentLibraryUserRepositoryUserDetailsServiceImpl documentLibraryUserRepositoryUserDetailsService = new DocumentLibraryUserRepositoryUserDetailsServiceImpl();
        documentLibraryUserRepositoryUserDetailsService.setDocumentLibraryUserRepository(documentLibraryUserRepository);
        documentLibraryUserRepositoryUserDetailsService.setDocumentLibraryUserSecurityUserFactory(new SecurityUserFactoryImpl());
        return documentLibraryUserRepositoryUserDetailsService;
    }

    @Bean
    public SecurityUserFactory<DocumentLibraryUser> documentLibraryUserSecurityUserFactory(){
        return new SecurityUserFactoryImpl();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().disable()
            .csrf()
                .csrfTokenRepository(csrfTokenRepository())
            .and()
            .sessionManagement()
                .enableSessionUrlRewriting(true)
            .and()
                .formLogin()
                    .usernameParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY)
                    .passwordParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY)
                    .loginProcessingUrl("/j_spring_security_check")
                    .loginPage(LOG_IN_URL_PAGE)
                    .defaultSuccessUrl(DEFAULT_TARGET_URL_PAGE)
                    .failureUrl(AUTHENTICATION_FAILURE_URL_PAGE)
            .and()
                .logout()
                .logoutUrl(LOG_OUT_URL_PAGE)
                .logoutSuccessUrl(LOG_OUT_SUCCESS_URL)
                .invalidateHttpSession(true)
            .and()
                .authorizeRequests()
                .antMatchers(LOG_IN_URL_PAGE,
                        LOG_OUT_URL_PAGE,
                        "/css/**",
                        "/js/**",
                        "/img/**",
                        "/**/favicon.ico",
                        "/webjars/**",
                        "/signup").permitAll()
                             .antMatchers("/**").fullyAuthenticated()
                             .anyRequest().authenticated()
            .and()
                .addFilterAfter(csrfHeaderFilter(), SessionManagementFilter.class);
     }

    private Filter csrfHeaderFilter() {
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request,
                                            HttpServletResponse response, FilterChain filterChain)
                    throws ServletException, IOException {
                CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class
                        .getName());
                if (csrf != null) {
                    Cookie cookie = WebUtils.getCookie(request, "XSRF-TOKEN");
                    String token = csrf.getToken();
                    if (cookie == null || token != null
                            && !token.equals(cookie.getValue())) {
                        cookie = new Cookie("XSRF-TOKEN", token);
                        cookie.setPath("/");
                        response.addCookie(cookie);
                    }
                }
                filterChain.doFilter(request, response);
            }
        };
    }

    private CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setHeaderName("X-XSRF-TOKEN");
        return repository;
    }

    @Autowired
    public void configureAuthenticationManagerBuilder(AuthenticationManagerBuilder auth,
                                                      DocumentLibraryUserRepository documentLibraryUserRepository) throws Exception {
                auth.userDetailsService(documentLibraryUserRepositoryUserDetailsService(documentLibraryUserRepository));
    }
}


