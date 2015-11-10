package it.valeriovaudi.documentlibrary.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Valerio on 19/05/2015.
 */

@Controller
public class BookShearchController {

    @RequestMapping("/bookSearch")
    public String initBookSearch(Model model){
        model.addAttribute("templatePath","bookSearch/bookSearch");
        model.addAttribute("withToolBoox",false);
        model.addAttribute("template","content");
        model.addAttribute("isActiveBookSearch",true);
        return "index";
    }
}
