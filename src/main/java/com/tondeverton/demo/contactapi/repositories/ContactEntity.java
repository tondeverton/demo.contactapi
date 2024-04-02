package com.tondeverton.demo.contactapi.repositories;

import java.util.UUID;

public class ContactEntity implements Contact, ContactToInsert, Cloneable {

    private UUID id;
    private String firstName;
    private String lastName;
    private String displayName;
    private String phoneNumber;
    private String email;

    @Override
    public UUID id() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public String firstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public String lastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String displayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String phoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String email() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public ContactEntity clone() {
        try {
            return (ContactEntity) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
