package com.tondeverton.demo.contactapi.testutilities;

import com.tondeverton.demo.contactapi.repositories.ContactToInsert;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FakerFactory {

    public static ContactToInsert contactToInsert() {
        var toInsert = mock(ContactToInsert.class);
        when(toInsert.getFirstName()).thenReturn(Faker.firstName());
        when(toInsert.getLastName()).thenReturn(Faker.lastName());
        when(toInsert.getDisplayName()).thenReturn(Faker.nickname());
        when(toInsert.getPhoneNumber()).thenReturn(Faker.phoneNumber());
        when(toInsert.getEmail()).thenReturn(Faker.email());

        return toInsert;
    }
}
