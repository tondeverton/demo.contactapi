package com.tondeverton.demo.contactapi.validators;

import com.tondeverton.demo.contactapi.providers.VariableProvider;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import static com.tondeverton.demo.contactapi.providers.Variables.CONTACTS_SEARCH_MAX_STRING_LENGTH;
import static org.apache.logging.log4j.util.Strings.isBlank;

public class ContactsSearchStringValidator implements ConstraintValidator<ContactsSearchString, String> {

    private final int maxSearchStringLength;

    public ContactsSearchStringValidator(VariableProvider variableProvider) {
        this.maxSearchStringLength = variableProvider.getValueAsInt(CONTACTS_SEARCH_MAX_STRING_LENGTH);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (isBlank(value))
            return true;

        return value.length() <= maxSearchStringLength;
    }
}
