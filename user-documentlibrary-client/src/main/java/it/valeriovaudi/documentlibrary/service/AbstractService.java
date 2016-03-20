package it.valeriovaudi.documentlibrary.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

/**
 * Created by Valerio on 28/02/2016.
 */
public abstract class AbstractService {

    protected Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    protected ObjectMapper objectMapper;

    protected ResponseEntity<String> getEmptyJsonObject(){
        return ResponseEntity.ok("{}");
    }

    protected ResponseEntity<String> getEmptyJsonArray(){
        return ResponseEntity.ok("[]");
    }
}
