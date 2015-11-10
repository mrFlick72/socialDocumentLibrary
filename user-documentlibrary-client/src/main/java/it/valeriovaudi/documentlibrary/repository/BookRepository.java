package it.valeriovaudi.documentlibrary.repository;

import it.valeriovaudi.documentlibrary.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Valerio on 26/05/2015.
 */
public interface BookRepository extends JpaRepository<Book,Long> {
}
