package it.valeriovaudi.documentlibrary.exception;

/**
 * Created by Valerio on 11/05/2015.
 */
public class BinaryInteractionAndManipulationException extends BookRepositoryServiceException{
    public BinaryInteractionAndManipulationException(Exception e) {
        super(e);
    }

    public BinaryInteractionAndManipulationException(String msg) {
        super(msg);
    }

    public BinaryInteractionAndManipulationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
