package it.valeriovaudi.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Created by Valerio on 01/09/2015.
 */

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = FileUploadValidator.class)
public @interface FileNotNull {
    String message() default "{it.valeriovaudi.documentlibrary.validator.FileUploadValidator.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}