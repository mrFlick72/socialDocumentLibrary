package it.valeriovaudi.documentlibrary.exception;

/**
 * Created by Valerio on 11/05/2015.
 */
public class PdfInteractionAndManipulation extends BookRepositoryServiceException{
    public PdfInteractionAndManipulation(Exception e) {
        super(e);
    }

    public PdfInteractionAndManipulation(String msg) {
        super(msg);
    }

    public PdfInteractionAndManipulation(String msg, Throwable cause) {
        super(msg, cause);
    }
}
