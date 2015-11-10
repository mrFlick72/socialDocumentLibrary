package it.valeriovaudi.documentlibrary.web.controller;

import it.valeriovaudi.documentlibrary.endpoint.BookServiceEndpoint;
import it.valeriovaudi.documentlibrary.web.model.BookMasterDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * Created by Valerio on 16/05/2015.
 */
@Controller
public class UploadBooksPageController {

    @Autowired
    private BookServiceEndpoint serviceEndpoint;

    public void setServiceEndpoint(BookServiceEndpoint serviceEndpoint) {
        this.serviceEndpoint = serviceEndpoint;
    }

    @ModelAttribute("searchTags")
    public List<String> searchTags(@Value("${search-book-service.searchMetadaTagService.baseUrl}") String searchMetadaTagServiceBaseUrl){
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(searchMetadaTagServiceBaseUrl,List.class);
    }

    @ModelAttribute("templatePath")
    public String getTemplatePath(){
        return "bookUpload/bookUploadPage";
    }

    @ModelAttribute("template")
    public String getTemplate(){
        return "content";
    }

    @ModelAttribute("bookMaster")
    public BookMasterDTO bookMasterDTO(){
        return new BookMasterDTO();
    }

    @RequestMapping("/uploadBook")
    public String initUploadBookPage(Model model,@RequestParam(value = "isLoaded",defaultValue = "false") boolean isLoaded) {
        model.addAttribute("isLoaded",isLoaded);
        model.addAttribute("isActiveUploadBook",true);

        return "index";
    }

    @RequestMapping(value = "/uploadBookFile", method = RequestMethod.POST,consumes = "multipart/form-data")
    public String uploadBookPage(Model model, @ModelAttribute("bookMaster") @Valid BookMasterDTO bookMasterDTO, BindingResult bindingResult) throws IOException {
        model.addAttribute("isActiveUploadBook",true);
        if(!bindingResult.hasErrors()){
            serviceEndpoint.saveBookDetails(bookMasterDTO);
        } else {
            return "/index";
        }
        return "redirect:uploadBook?isLoaded=true";
    }

}
