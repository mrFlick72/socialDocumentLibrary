package it.valeriovaudi.documentlibrary.exception;

/**
 * Created by Valerio on 11/05/2015.
 */
public class FileInteractionException extends BookRepositoryServiceException {

    public FileInteractionException(Exception e) {
        super(e);
    }

    public FileInteractionException(String msg) {
        super(msg);
    }

    public FileInteractionException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
