package it.valeriovaudi.documentlibrary.repository;

import it.valeriovaudi.documentlibrary.model.UserBookPreferedList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

/**
 * Created by Valerio on 26/05/2015.
 */
public interface UserBookPreferedListRepository extends JpaRepository<UserBookPreferedList,Long> {
    UserBookPreferedList findByUserName(@Param("userNameParam") String userNameParam);
}
