package com.tondeverton.demo.contactapi.entrypoints.rest.v1.reqsress;

import com.tondeverton.demo.contactapi.repositories.ContactToSave;

public class SaveContactRequest implements ContactToSave {
    private String firstName;
    private String lastName;
    private String displayName;
    private String phoneNumber;
    private String email;

    public SaveContactRequest(String firstName, String lastName, String displayName, String phoneNumber, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.displayName = displayName;
        this.phoneNumber = phoneNumber;
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
