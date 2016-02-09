package it.valeriovaudi.validator;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by Valerio on 01/09/2015.
 */
public class FileUploadValidator implements ConstraintValidator<FileNotNull,MultipartFile>  {


    @Override
    public void initialize(FileNotNull constraintAnnotation) {

    }

    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        return !value.isEmpty();
    }
}
