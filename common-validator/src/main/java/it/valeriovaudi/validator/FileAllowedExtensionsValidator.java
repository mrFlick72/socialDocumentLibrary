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
public class FileAllowedExtensionsValidator implements ConstraintValidator<FileAllowedExtensions,MultipartFile> {

    private String[] allowedExtensions;

    @Override
    public void initialize(FileAllowedExtensions constraintAnnotation) {
        allowedExtensions = constraintAnnotation.allowedExtensions();
    }

    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        if(!value.isEmpty()){
            String originalFilename = value.getOriginalFilename();
            String[] split = originalFilename.split("\\.");
            String fileExternsion = split[split.length-1].toUpperCase();
            Set<String> collect = Stream.of(allowedExtensions)
                                        .map(String::toUpperCase)
                                        .collect(Collectors.toSet());
            return collect.contains(fileExternsion);
        }

        return true;
    }
}
