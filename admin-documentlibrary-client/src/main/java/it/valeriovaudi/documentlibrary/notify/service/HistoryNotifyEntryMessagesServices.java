package it.valeriovaudi.documentlibrary.notify.service;

import it.valeriovaudi.documentlibrary.notify.model.HistoryNotifyEntry;
import it.valeriovaudi.documentlibrary.notify.repository.HistoryNotifyEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.json.Json;
import java.util.List;
import java.util.Locale;

/**
 * Created by Valerio on 16/09/2015.
 */

@RestController
@RequestMapping("/historyNotifyEntity/messages")
public class HistoryNotifyEntryMessagesServices {

    private MessageSource messageSource;

    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @RequestMapping
    public ResponseEntity<String> getAdminBookListMessages(Locale locale) {
        String body = Json.createObjectBuilder()
                .add("header",Json.createObjectBuilder()
                        .add("label", messageSource.getMessage("notify.header.label", new Object[]{}, locale))
                        .add("bookName", messageSource.getMessage("notify.header.bookName.label", new Object[]{}, locale))
                        .add("options", messageSource.getMessage("notify.header.options.label", new Object[]{}, locale)))
                .add("noNotificationLabel", messageSource.getMessage("notify.noNotification.label", new Object[]{}, locale))
                .add("deleteBookNotifyDialog", Json.createObjectBuilder()
                        .add("firstPart", messageSource.getMessage("notify.deleteBookNotifyDialog.firstPart.label", new Object[]{}, locale))
                        .add("secondPart", messageSource.getMessage("notify.deleteBookNotifyDialog.secondPart.label", new Object[]{}, locale))
                        .add("yesButton", messageSource.getMessage("notify.deleteBookNotifyDialog.yesButton.label", new Object[]{}, locale))
                        .add("noButton", messageSource.getMessage("notify.deleteBookNotifyDialog.noButton.label", new Object[]{}, locale)))
                .add("notifyErrorDialog", Json.createObjectBuilder().add("okButton", messageSource.getMessage("notify.notifyErrorDialog.okButton.label", new Object[]{}, locale)))
                .build()
                .toString();

        return ResponseEntity.ok(body);
    }
}
