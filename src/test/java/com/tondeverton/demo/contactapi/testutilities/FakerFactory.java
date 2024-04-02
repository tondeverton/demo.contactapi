package com.tondeverton.demo.contactapi.testutilities;

import com.tondeverton.demo.contactapi.repositories.ContactEntity;
import com.tondeverton.demo.contactapi.repositories.ContactToSave;

import java.util.UUID;

public class FakerFactory {

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

    public static ContactToSave contactToSave() {
        return new ContactToSave() {
            @Override
            public String getFirstName() {
                return Faker.firstName();
            }

            @Override
            public String getLastName() {
                return Faker.lastName();
            }

            @Override
            public String getDisplayName() {
                return Faker.nickname();
            }

            @Override
            public String getPhoneNumber() {
                return Faker.phoneNumber();
            }

            @Override
            public String getEmail() {
                return Faker.email();
            }
        };
    }
}
