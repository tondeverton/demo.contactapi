package com.tondeverton.demo.contactapi.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {

    public static final int PHONE_NUMBER_MAX_SIZE = 20;
    public static final String REGEX_ONLY_NUMBERS = "[0-9]+";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null || value.isBlank()) {
            return true;
        }

        if (value.length() > PHONE_NUMBER_MAX_SIZE) {
            return false;
        }

        return value.matches(REGEX_ONLY_NUMBERS);
    }
}
