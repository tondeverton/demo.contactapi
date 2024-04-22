package com.tondeverton.demo.contactapi.exceptions;

public class ContactNotFoundException extends PreconditionException {
    public ContactNotFoundException() {
        super("Contact not found");
    }
}
