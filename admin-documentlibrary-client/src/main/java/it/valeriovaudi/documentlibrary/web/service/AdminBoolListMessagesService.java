package it.valeriovaudi.documentlibrary.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.json.Json;
import java.util.Locale;

/**
 * Created by Valerio on 26/08/2015.
 */
@RestController
@RequestMapping("/adminBookList/messages")
public class AdminBoolListMessagesService {

    private MessageSource messageSource;

    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @RequestMapping
    public ResponseEntity<String> getAdminBookListMessages(Locale locale){
        String body = Json.createObjectBuilder()
                .add("headelLabel", messageSource.getMessage("adminBookList.header.label", new Object[]{}, locale))
                .add("tableHeader", Json.createObjectBuilder()
                        .add("bookNameLabel", messageSource.getMessage("adminBookList.tableHeader.bookName.label", new Object[]{}, locale))
                        .add("authorLabel", messageSource.getMessage("adminBookList.tableHeader.author.label", new Object[]{}, locale))
                        .add("actionLabel", messageSource.getMessage("adminBookList.tableHeader.action.label", new Object[]{}, locale))
                        .add("publishedLabel", messageSource.getMessage("adminBookList.tableHeader.publishedLabel", new Object[]{}, locale)))
                .add("optionsTableCell", Json.createObjectBuilder()
                        .add("optionsLabel", messageSource.getMessage("adminBookList.optionsTableCell.options.label", new Object[]{}, locale))
                        .add("publishLabeld", messageSource.getMessage("adminBookList.optionsTableCell.publish.label", new Object[]{}, locale))
                        .add("unPublishLabeld", messageSource.getMessage("adminBookList.optionsTableCell.unPublish.label", new Object[]{}, locale))
                        .add("deleteLabel", messageSource.getMessage("adminBookList.optionsTableCell.delete.label", new Object[]{}, locale)))
                .add("deleteConfirmPopUp", Json.createObjectBuilder()
                        .add("headerLabel", messageSource.getMessage("adminBookList.deleteConfirmPopUp.header.label", new Object[]{}, locale))
                        .add("messagePartOneLabel", messageSource.getMessage("adminBookList.deleteConfirmPopUp.messagePartOne.label", new Object[]{}, locale))
                        .add("messagePartTwoLabel", messageSource.getMessage("adminBookList.deleteConfirmPopUp.messagePartTwo.label", new Object[]{}, locale))
                        .add("affermativeLabel", messageSource.getMessage("adminBookList.deleteConfirmPopUp.affermative.label", new Object[]{}, locale))
                        .add("negativeLabel", messageSource.getMessage("adminBookList.deleteConfirmPopUp.negative.label", new Object[]{}, locale)))
                .add("optionDialogPopUp", Json.createObjectBuilder()
                        .add("headerLabel", messageSource.getMessage("adminBookList.optionDialogPopUp.header.label", new Object[]{}, locale))
                        .add("publishLabel", messageSource.getMessage("adminBookList.optionDialogPopUp.publish.label", new Object[]{}, locale))
                        .add("tagsLabel", messageSource.getMessage("adminBookList.optionDialogPopUp.tags.label", new Object[]{}, locale))
                        .add("authorLabel", messageSource.getMessage("adminBookList.optionDialogPopUp.author.label", new Object[]{}, locale))
                        .add("descrptionLabel", messageSource.getMessage("adminBookList.optionDialogPopUp.descrption.label", new Object[]{}, locale))
                        .add("okLabel", messageSource.getMessage("adminBookList.optionDialogPopUp.ok.label", new Object[]{}, locale))
                        .add("closeLabel", messageSource.getMessage("adminBookList.optionDialogPopUp.close.label", new Object[]{}, locale))
                        .add("tagsPlaceholder", messageSource.getMessage("adminBookList.optionDialogPopUp.tags.placeholder.label", new Object[]{}, locale)))
                .build()
                .toString();

        return ResponseEntity.ok(body);
    }
}
