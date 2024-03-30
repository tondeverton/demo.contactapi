package com.tondeverton.demo.contactapi.validators;

import com.tondeverton.demo.contactapi.testutilities.Faker;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

public class PhoneNumberValidatorTest {

    private final PhoneNumberValidator validator;

    public PhoneNumberValidatorTest() {
        this.validator = new PhoneNumberValidator();
    }

    @Test
    void isValid__givenValueEqualToNull__shouldReturnsTrue() {
        var result = validator.isValid(null, mock(ConstraintValidatorContext.class));

        assertTrue(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   "})
    void isValid__givenValueEqualToEmptyOrWhiteSpaces__shouldReturnsTrue(String value) {
        var result = validator.isValid(value, mock(ConstraintValidatorContext.class));

        assertTrue(result);
    }

    @ParameterizedTest
    @ValueSource(ints = {21, 22, 30, 75, 97})
    void isValid__givenValueWithSizeGreaterThanPhoneNumberMaxSize__shouldReturnsFalse(int valueSize) {
        var result = validator.isValid(Faker.word(valueSize), mock(ConstraintValidatorContext.class));

        assertFalse(result);
    }

    @ParameterizedTest
    @ValueSource(ints = {8, 13, 16, 18, 20})
    void isValid__givenValueWithSizeLowerThanPhoneNumberMaxSizeAndContainingLetters__shouldReturnsFalse(int valueSize) {
        var result = validator.isValid(Faker.word(valueSize), mock(ConstraintValidatorContext.class));

        assertFalse(result);
    }

    @ParameterizedTest
    @ValueSource(ints = {8, 13, 16, 18, 20})
    void isValid__givenValueWithSizeLowerThanPhoneNumberMaxSizeAndContainingOnlyNumbers__shouldReturnsTrue(int valueSize) {
        var result = validator.isValid(Faker.number(valueSize), mock(ConstraintValidatorContext.class));

        assertTrue(result);
    }
}
