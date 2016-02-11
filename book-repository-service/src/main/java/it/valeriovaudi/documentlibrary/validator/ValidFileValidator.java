package it.valeriovaudi.documentlibrary.validator;

import it.valeriovaudi.documentlibrary.model.PdfBookMaster;
import it.valeriovaudi.validator.FileAllowedContentType;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

/**
 * Created by Valerio on 01/09/2015.
 */
public class ValidFileValidator implements ConstraintValidator<ValidFile,PdfBookMaster> {

    private String[] allowedContentType;
    private boolean useExplicitContentType;
    @Override
    public void initialize(ValidFile constraintAnnotation) {
        this.allowedContentType=constraintAnnotation.allowedContentType();
        this.useExplicitContentType = constraintAnnotation.useExplicitContentType();
    }

    @Override
    public boolean isValid(PdfBookMaster value, ConstraintValidatorContext context) {
        if(value != null) {
            return useExplicitContentType && value.getBookFile() != null ?
                    Arrays.asList(this.allowedContentType).contains(value.getContentType()) :
                    Arrays.asList(this.allowedContentType).contains(value.getBookFile().getContentType());
        }
        return false;
    }
}
