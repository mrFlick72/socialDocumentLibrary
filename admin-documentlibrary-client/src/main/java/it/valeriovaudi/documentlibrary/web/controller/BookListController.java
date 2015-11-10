package it.valeriovaudi.documentlibrary.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Valerio on 29/05/2015.
 */

@Controller
public class BookListController {

    @RequestMapping("/bookList")
    public String getIndex(Model model){
        model.addAttribute("templatePath","bookList/bookList");
        model.addAttribute("template","content");
        model.addAttribute("isActiveBookList",true);
        return "index";
    }
}
