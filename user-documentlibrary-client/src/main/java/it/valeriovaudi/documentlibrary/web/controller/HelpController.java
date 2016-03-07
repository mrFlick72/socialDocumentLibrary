package it.valeriovaudi.documentlibrary.web.controller;

import it.valeriovaudi.documentlibrary.model.DocumentLibraryUser;
import it.valeriovaudi.documentlibrary.repository.DocumentLibraryUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;

/**
 * Created by Valerio on 28/08/2015.
 */
@Controller
public class HelpController {

    @Autowired
    private DocumentLibraryUserRepository documentLibraryUserRepository;

    public void setDocumentLibraryUserRepository(DocumentLibraryUserRepository documentLibraryUserRepository) {
        this.documentLibraryUserRepository = documentLibraryUserRepository;
    }

    @RequestMapping("/shortHelp")
    public String showShortHelp(){
        return "help/help";
    }

    @RequestMapping("/short/noContentHelp")
    public String noContentHelp(){
        return "help/content/noContent";
    }

    @RequestMapping("/shortHelp/noContentHelp")
     public String noContentShortHelp(){
        return "help/shortContent/noContent";
    }

    @RequestMapping("/short/searchContentHelp")
    public String searchHelp(){
        return "help/content/searchContent";
    }

    @RequestMapping("/shortHelp/searchContentHelp")
    public String searchShortHelp(){
        return "help/shortContent/searchContent";
    }

    @RequestMapping("/short/myBookListContentHelp")
    public String myBookListHelp(){
        return "help/content/myBookListContent";
    }

    @RequestMapping("/shortHelp/myBookListContentHelp")
    public String myBookListShortHelp(){
        return "help/shortContent/myBookListContent";
    }

    @RequestMapping("/short/readBookContentHelp")
    public String readBookHelp(){
        return "help/content/readBookContent";
    }

    @RequestMapping("/shortHelp/readBookContentHelp")
    public String readBookShortHelp(){
        return "help/shortContent/readBookContent";
    }


    @Transactional
    @RequestMapping(value = "/preference/shortHelp/show", method = RequestMethod.PUT)
    public ResponseEntity<Void> showShortHelp(Principal principal){
        DocumentLibraryUser byUserName = documentLibraryUserRepository.findByUserName(principal.getName());
        byUserName.setShowShortHelp(true);
        return ResponseEntity.noContent().build();
    }

    @Transactional
    @RequestMapping(value = "/preference/shortHelp/hide", method = RequestMethod.PUT)
    public ResponseEntity<Void> hideShortHelp(Principal principal){
        DocumentLibraryUser byUserName = documentLibraryUserRepository.findByUserName(principal.getName());
        byUserName.setShowShortHelp(false);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/preference/shortHelp", method = RequestMethod.GET)
    public ResponseEntity<Boolean> getShortHelpState(Principal principal){
        DocumentLibraryUser byUserName = documentLibraryUserRepository.findByUserName(principal.getName());
        return ResponseEntity.ok().body(byUserName.isShowShortHelp());
    }
}
