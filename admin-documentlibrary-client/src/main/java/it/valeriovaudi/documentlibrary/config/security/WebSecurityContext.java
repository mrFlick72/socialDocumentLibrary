package it.valeriovaudi.documentlibrary.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebMvcSecurity
@EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled = true)
public class WebSecurityContext extends WebSecurityConfigurerAdapter {

    /*******************************************Log-In properties******************************************************/
    private static final String LOG_IN_URL_PAGE = "/signin";
    private static final String DEFAULT_TARGET_URL_PAGE = "/bookList";
    private static final String AUTHENTICATION_FAILURE_URL_PAGE = "/signin?error=1";

    /*******************************************Log-Out properties*****************************************************/
    private static final String LOG_OUT_URL_PAGE = "/logout";
    private static final String LOG_OUT_SUCCESS_URL = "/signin";


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
            .headers().disable()
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
                             .anyRequest().authenticated();
     }

    @Autowired
    public void configureAuthenticationManagerBuilder(AuthenticationManagerBuilder auth) throws Exception {
                auth.inMemoryAuthentication()
                .withUser("admin")
                .roles("USER")
                .password("admin");
    }
}


