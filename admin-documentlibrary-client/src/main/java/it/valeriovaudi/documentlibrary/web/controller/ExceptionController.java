package it.valeriovaudi.documentlibrary.web.controller;

import org.apache.tomcat.util.http.fileupload.FileUploadBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * Created by Valerio on 09/10/2015.
 */
@Controller
public class ExceptionController {

    @Autowired
    private MessageSource messageSource;

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @RequestMapping("/exception")
    public String exception(Model model, Exception ex, Locale locale, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {

        model.addAttribute("templatePath", "exception/exception");
        model.addAttribute("template", "content");
        try {
            model.addAttribute("exceptionMessage", messageSource.getMessage(String.format("exception.body.%s", httpResponse.getStatus()), new Object[]{}, locale));
        } catch (NoSuchMessageException e) {
            model.addAttribute("exceptionMessage", messageSource.getMessage("exception.body", new Object[]{}, locale));
        }
        return "index";
    }

}

