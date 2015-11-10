package it.valeriovaudi.documentlibrary.web.controller;

import it.valeriovaudi.documentlibrary.config.security.WebSecurityContext;
import it.valeriovaudi.documentlibrary.model.DocumentLibraryUser;
import it.valeriovaudi.documentlibrary.model.UserBookPreferedList;
import it.valeriovaudi.documentlibrary.model.builder.DocumentLibraryUserBuilder;
import it.valeriovaudi.documentlibrary.repository.DocumentLibraryUserRepository;
import it.valeriovaudi.documentlibrary.repository.UserBookPreferedListRepository;
import it.valeriovaudi.documentlibrary.security.SecurityUserFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.ArrayList;

/**
 * Created by Valerio on 18/05/2015.
 */

@Controller
public class SingUpController {
    private static final String SIGNUP_VIEW_NAME = "signup/signup";

    @Autowired
    private SecurityUserFactory<DocumentLibraryUser> documentLibraryUserSecurityUserFactory;

    @Autowired
    private DocumentLibraryUserRepository documentLibraryUserRepository;

    @Autowired
    private UserBookPreferedListRepository userBookPreferedListRepository;

    public void setUserBookPreferedListRepository(UserBookPreferedListRepository userBookPreferedListRepository) {
        this.userBookPreferedListRepository = userBookPreferedListRepository;
    }

    public void setDocumentLibraryUserRepository(DocumentLibraryUserRepository documentLibraryUserRepository) {
        this.documentLibraryUserRepository = documentLibraryUserRepository;
    }

    public void setDocumentLibraryUserSecurityUserFactory(SecurityUserFactory<DocumentLibraryUser> documentLibraryUserSecurityUserFactory) {
        this.documentLibraryUserSecurityUserFactory = documentLibraryUserSecurityUserFactory;
    }

    @RequestMapping(value = "/signup")
    public String signup(Model model) {
        model.addAttribute("signupForm",new DocumentLibraryUser());
        return SIGNUP_VIEW_NAME;
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String doSignup(@ModelAttribute("signupForm") @Valid DocumentLibraryUser documentLibraryUser, Errors errors) {
        if (errors.hasErrors()) {
            return SIGNUP_VIEW_NAME;
        }

        try{
            DocumentLibraryUser save = documentLibraryUserRepository.save(DocumentLibraryUserBuilder
                                                                            .newDocumentLibraryUserBuilder(documentLibraryUser)
                                                                            .showShortHelp(true)
                                                                            .build());
            UserBookPreferedList userBookPreferedList = new UserBookPreferedList();
            userBookPreferedList.setBooksReadList(new ArrayList<>());
            userBookPreferedList.setDocumentLibraryUser(documentLibraryUser);

            userBookPreferedListRepository.save(userBookPreferedList);
            SecurityContextHolder.getContext().setAuthentication(documentLibraryUserSecurityUserFactory.getAutenticatedUser(save));
        } catch (DataAccessException e){
            errors.rejectValue("userName", "userName.error.label");
            return SIGNUP_VIEW_NAME;
        }

        return String.format("redirect:/%s", WebSecurityContext.DEFAULT_TARGET_URL_PAGE);
    }
}
