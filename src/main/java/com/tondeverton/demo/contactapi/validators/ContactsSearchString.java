package com.tondeverton.demo.contactapi.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD, ANNOTATION_TYPE, PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = ContactsSearchStringValidator.class)
@Documented
public @interface ContactsSearchString {
    String message() default "is not valid search string";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
