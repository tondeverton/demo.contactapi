package com.tondeverton.demo.contactapi.testutilities;

import com.tondeverton.demo.contactapi.repositories.ContactEntity;
import com.tondeverton.demo.contactapi.repositories.ContactToInsert;

import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FakerFactory {

    public static ContactToInsert contactToInsert() {
        return contactEntity();
    }

    public static ContactEntity contactEntity() {
        var contact = new ContactEntity();
        contact.setId(UUID.randomUUID());
        contact.setFirstName(Faker.firstName());
        contact.setLastName(Faker.lastName());
        contact.setDisplayName(Faker.nickname());
        contact.setPhoneNumber(Faker.phoneNumber());
        contact.setEmail(Faker.email());

        return contact;
    }
}
