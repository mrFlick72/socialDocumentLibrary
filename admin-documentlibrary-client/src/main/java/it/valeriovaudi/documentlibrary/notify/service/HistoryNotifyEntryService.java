package it.valeriovaudi.documentlibrary.notify.service;

import it.valeriovaudi.documentlibrary.notify.model.HistoryNotifyEntry;
import it.valeriovaudi.documentlibrary.notify.repository.HistoryNotifyEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import javax.json.Json;
import java.util.List;

/**
 * Created by Valerio on 16/09/2015.
 */

@Service
public class HistoryNotifyEntryService {

    @Autowired
    private HistoryNotifyEntryRepository historyNotifyEntryRepository;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    public void setHistoryNotifyEntryRepository(HistoryNotifyEntryRepository historyNotifyEntryRepository) {
        this.historyNotifyEntryRepository = historyNotifyEntryRepository;
    }

    public void setSimpMessagingTemplate(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public HistoryNotifyEntry createNotify(String bookName,String bookId){
        HistoryNotifyEntry historyNotifyEntry = new HistoryNotifyEntry();
        historyNotifyEntry.setBookId(bookId);
        historyNotifyEntry.setBookName(bookName);

        HistoryNotifyEntry savedHistoryNotifyEntry = historyNotifyEntryRepository.save(historyNotifyEntry);
        brodcastNewEntityHistory(savedHistoryNotifyEntry);
        brodcastHistory();
        return savedHistoryNotifyEntry;
    }

    public List<HistoryNotifyEntry> readNotifys(){
        List<HistoryNotifyEntry> all = historyNotifyEntryRepository.findAll();
        brodcastHistory(all);
        return all;
    }

    public boolean deleteNotifyByBookId(String bookId){
        historyNotifyEntryRepository.deleteByBookId(bookId);
        brodcastHistory();
        return historyNotifyEntryRepository.findByBookId(bookId)==null;
    }

    private void brodcastNewEntityHistory(HistoryNotifyEntry savedHistoryNotifyEntry){
        simpMessagingTemplate.convertAndSend("/notify/createBookJob/new",savedHistoryNotifyEntry);
    }

    private void brodcastHistory(){
        List<HistoryNotifyEntry> all = historyNotifyEntryRepository.findAll();
        simpMessagingTemplate.convertAndSend("/notify/createBookJob/end", all);
    }

    private void brodcastHistory(List<HistoryNotifyEntry> all){
        simpMessagingTemplate.convertAndSend("/notify/createBookJob/end", all);
    }

    private void brodcastHistory(HistoryNotifyEntry notifyEntry){
        simpMessagingTemplate.convertAndSend("/notify/createBookJob/end", notifyEntry);
    }
}
