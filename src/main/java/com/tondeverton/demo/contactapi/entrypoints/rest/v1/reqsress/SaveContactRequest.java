package com.tondeverton.demo.contactapi.entrypoints.rest.v1.reqsress;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.tondeverton.demo.contactapi.repositories.ContactToSave;

import static com.tondeverton.demo.contactapi.utilities.Regex.getRegexUtil;

public class SaveContactRequest implements ContactToSave {
    private final String firstName;
    private final String lastName;
    private final String displayName;
    private final String phoneNumber;
    private final String email;

    @JsonCreator
    public SaveContactRequest(String firstName, String lastName, String displayName, String phoneNumber, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.displayName = displayName;
        this.phoneNumber = getRegexUtil().removeAlphaCharacter(phoneNumber);
        this.email = email;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public String getEmail() {
        return email;
    }
}
