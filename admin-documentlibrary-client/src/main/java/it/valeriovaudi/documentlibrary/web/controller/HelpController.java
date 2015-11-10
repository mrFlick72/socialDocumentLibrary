package it.valeriovaudi.documentlibrary.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Valerio on 14/10/2015.
 */
@Controller
public class HelpController {

    @RequestMapping("/help")
    public String initHelp(Model model){
        model.addAttribute("templatePath","help/help");
        model.addAttribute("template","content");
        model.addAttribute("isActiveHelp",true);
        return "index";
    }

    @RequestMapping("/help/uploadBookHelpContent")
    public String uploadBookContent(){
        return "help/content/uploadBookHelpContent";
    }

    @RequestMapping("/help/adminBookListHelpContent")
    public String uploadBookHelpContent(){
        return "help/content/adminBookListHelpContent";
    }
}
