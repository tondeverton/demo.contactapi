package com.tondeverton.demo.contactapi.entrypoints.rest.v1.reqsress;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tondeverton.demo.contactapi.repositories.Contact;

import java.util.Collection;

public record GetAllContactsResponse(
        int page,
        int totalPages,
        @JsonIgnore Collection<Contact> contacts
) {
    public Collection<ContactResponse> getItems() {
        return contacts.stream().map(ContactResponse::new).toList();
    }
}
