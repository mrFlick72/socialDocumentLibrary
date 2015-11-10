package it.valeriovaudi.documentlibrary.exception;

import org.springframework.core.NestedRuntimeException;

/**
 * Created by Valerio on 11/05/2015.
 */
public abstract class BookRepositoryServiceException extends NestedRuntimeException {

    public BookRepositoryServiceException(Exception e){
        super(e.getMessage(),e.getCause());
    }

    public BookRepositoryServiceException(String msg) {
        super(msg);
    }

    public BookRepositoryServiceException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
