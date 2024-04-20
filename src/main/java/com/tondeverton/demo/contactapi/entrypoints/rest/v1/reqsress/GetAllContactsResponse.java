package com.tondeverton.demo.contactapi.entrypoints.rest.v1.reqsress;

import com.tondeverton.demo.contactapi.repositories.Contact;

import java.util.Collection;

public record GetAllContactsResponse(
        int page,
        int totalPages,
        Collection<Contact> items
) {
}
