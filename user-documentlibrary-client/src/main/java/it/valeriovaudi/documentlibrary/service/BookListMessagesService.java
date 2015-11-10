package it.valeriovaudi.documentlibrary.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import java.util.Locale;

/**
 * Created by Valerio on 26/08/2015.
 */
@RestController
@RequestMapping("/bookList/messages")
public class BookListMessagesService {

    private MessageSource messageSource;

    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @RequestMapping
    public ResponseEntity<String> getBookListMessages(Locale locale){
        JsonObjectBuilder createObjectBuilder = Json.createObjectBuilder();

        createObjectBuilder.add("listHeaderLabel", messageSource.getMessage("bookList.listHeader.label", new Object[]{}, locale))
                .add("removeBookLabel", messageSource.getMessage("bookList.removeBook.label", new Object[]{}, locale))
                .add("openBookLabel", messageSource.getMessage("bookList.openBook.label", new Object[]{}, locale));

        createObjectBuilder.add("feedbackPanel", Json.createObjectBuilder()
                .add("headerLabel", messageSource.getMessage("bookist.feedbackPanel.header.label", new Object[]{}, locale))
                .add("bodyLabel", messageSource.getMessage("bookList.feedbackPanel.body.label", new Object[]{}, locale))
                .add("scoreSubmitLabel", messageSource.getMessage("bookList.feedbackPanel.scoreSubmit.label", new Object[]{}, locale)));

        createObjectBuilder.add("feedbackPopUp", Json.createObjectBuilder()
                        .add("headerLabel", messageSource.getMessage("bookList.feedbackPopUp.header.label", new Object[]{}, locale))
                        .add("titleLabel", messageSource.getMessage("bookList.feedbackPopUp.title.label", new Object[]{}, locale))
                        .add("bodyLabel", messageSource.getMessage("bookList.feedbackPopUp.body.label", new Object[]{}, locale))
                        .add("scoreLabel", messageSource.getMessage("bookList.feedbackPopUp.score.label", new Object[]{}, locale))
                        .add("okButtonLabel", messageSource.getMessage("bookList.feedbackPopUp.okButton.label", new Object[]{}, locale))
                        .add("closeButtonLabel", messageSource.getMessage("bookList.feedbackPopUp.closeButton.label", new Object[]{}, locale)));

        createObjectBuilder.add("deleteBookConfirmDialog",Json.createObjectBuilder()
                        .add("headerLabel", messageSource.getMessage("bookList.deleteBookConfirmDialog.header.label", new Object[]{}, locale))
                        .add("bodyPartOneLabel", messageSource.getMessage("bookList.deleteBookConfirmDialog.bodyPartOne.label", new Object[]{}, locale))
                        .add("bodyPartTwoLabel", messageSource.getMessage("bookList.deleteBookConfirmDialog.bodyPartTwo.label", new Object[]{}, locale))
                        .add("okButtonLabel", messageSource.getMessage("bookList.deleteBookConfirmDialog.okButton.label", new Object[]{}, locale))
                        .add("closeButtonLabel", messageSource.getMessage("bookList.deleteBookConfirmDialog.closeButton.label", new Object[]{}, locale)));

        String body = createObjectBuilder.build()
                .toString();

        return ResponseEntity.ok(body);
    }

}
