package it.valeriovaudi.documentlibrary.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Valerio on 18/05/2015.
 */
@Controller
public class BookUserListController {

    @RequestMapping("/bookUserList")
    public String initBookUserList(Model model){
        model.addAttribute("templatePath","bookUserList/bookUserList");
        model.addAttribute("template","content");
        model.addAttribute("withToolBoox",false);
        model.addAttribute("isActiveBookUserList",true);

        return "index";
    }
}
