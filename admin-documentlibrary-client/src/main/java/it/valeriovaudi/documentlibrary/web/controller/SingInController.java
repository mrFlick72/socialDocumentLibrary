package it.valeriovaudi.documentlibrary.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Valerio on 18/05/2015.
 */

@Controller
public class SingInController {

    private static final String SIGNIN_VIEW_NAME = "signin/signin";

    @RequestMapping(value = "/signin")
    public String signin() {
        return SIGNIN_VIEW_NAME;
    }

}
