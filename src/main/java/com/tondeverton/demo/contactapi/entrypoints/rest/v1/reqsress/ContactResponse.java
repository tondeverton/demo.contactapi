package com.tondeverton.demo.contactapi.entrypoints.rest.v1.reqsress;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.tondeverton.demo.contactapi.repositories.Contact;

import java.util.UUID;

public record ContactResponse(@JsonIgnoreProperties("identifier") @JsonUnwrapped Contact contact) {
    public UUID getId() {
        return contact.getIdentifier();
    }
}
