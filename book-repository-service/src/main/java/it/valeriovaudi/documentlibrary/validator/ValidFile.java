package it.valeriovaudi.documentlibrary.validator;

import it.valeriovaudi.validator.FileAllowedContentTypeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Created by Valerio on 01/09/2015.
 */

@Target({ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ValidFileValidator.class)
public @interface ValidFile {
    String message() default "{it.valeriovaudi.documentlibrary.validator.ValidFile.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    boolean useExplicitContentType() default true;
    String[] allowedContentType() default {};
}