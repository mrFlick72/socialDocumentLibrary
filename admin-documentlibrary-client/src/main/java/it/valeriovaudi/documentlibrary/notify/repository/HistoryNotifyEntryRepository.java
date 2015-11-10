package it.valeriovaudi.documentlibrary.notify.repository;

import it.valeriovaudi.documentlibrary.notify.model.HistoryNotifyEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

/**
 * Created by Valerio on 16/09/2015.
 */
@Transactional
public interface HistoryNotifyEntryRepository extends JpaRepository<HistoryNotifyEntry,Long>{

    @Modifying
    void deleteByBookId(@Param(value = "bookIdAux") String bookId);
    HistoryNotifyEntry findByBookId(@Param(value = "bookIdAux") String bookId);
}
