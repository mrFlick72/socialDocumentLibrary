package it.valeriovaudi.documentlibrary.web.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Created by Valerio on 09/10/2015.
 */
@ControllerAdvice
public class AdviceExceptionController {

    @ExceptionHandler(value = org.springframework.web.multipart.MultipartException.class)
    public String exceptionHAndler(Model model,Exception ex){
        model.addAttribute("templatePath", "exception/exception");
        model.addAttribute("withToolBoox", false);
        model.addAttribute("template", "content");

        return "index";
    }
}
