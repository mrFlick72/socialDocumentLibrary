package it.valeriovaudi.validator;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Valerio on 01/09/2015.
 */
public class FileAllowedContentTypeValidator implements ConstraintValidator<FileAllowedContentType,MultipartFile> {

    private String[] allowedContentType;

    @Override
    public void initialize(FileAllowedContentType constraintAnnotation) {
        allowedContentType = constraintAnnotation.allowedContentType();
    }

    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        if(!value.isEmpty()){
            String originalFilename = value.getContentType();
            return value.getContentType().contains(originalFilename);
        }

        return true;
    }
}
