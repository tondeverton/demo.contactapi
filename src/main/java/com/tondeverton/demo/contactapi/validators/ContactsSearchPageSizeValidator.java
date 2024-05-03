package com.tondeverton.demo.contactapi.validators;

import com.tondeverton.demo.contactapi.providers.VariableProvider;
import com.tondeverton.demo.contactapi.providers.Variables;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ContactsSearchPageSizeValidator implements ConstraintValidator<ContactsSearchPageSize, Integer> {

    private final int maxPageSize;

    public ContactsSearchPageSizeValidator(VariableProvider variableProvider) {
        maxPageSize = variableProvider.getValueAsInt(Variables.CONTACTS_SEARCH_MAX_PAGE_SIZE);
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return value <= maxPageSize;
    }
}
