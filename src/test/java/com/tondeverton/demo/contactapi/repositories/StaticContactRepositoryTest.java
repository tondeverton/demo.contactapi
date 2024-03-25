package com.tondeverton.demo.contactapi.repositories;

import com.tondeverton.demo.contactapi.testutilities.Faker;
import com.tondeverton.demo.contactapi.utilities.StringSimilarityUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class StaticContactRepositoryTest {

    private final StringSimilarityUtil stringSimilarity;
    private final StaticContactRepository repository;

    public StaticContactRepositoryTest() {
        stringSimilarity = mock(StringSimilarityUtil.class);
        this.repository = new StaticContactRepository(stringSimilarity);
    }

    @BeforeEach
    public void resetMocks() {
        reset(stringSimilarity);
    }

    @Test
    void add_givenSomeContactToInsert_shouldReturnsContactWithTheSamePropertiesFromInput() {
        var toInsert = mock(ContactToInsert.class);

        var someFirstName = Faker.firstName();
        when(toInsert.getFirstName()).thenReturn(someFirstName);
        var someLastName = Faker.lastName();
        when(toInsert.getLastName()).thenReturn(someLastName);
        var someDisplayName = Faker.nickname();
        when(toInsert.getDisplayName()).thenReturn(someDisplayName);
        var somePhoneNumber = Faker.phoneNumber();
        when(toInsert.getPhoneNumber()).thenReturn(somePhoneNumber);
        var someEmail = Faker.email();
        when(toInsert.getEmail()).thenReturn(someEmail);

        var persisted = repository.add(toInsert);

        assertEquals(someFirstName, persisted.getFirstName());
        assertEquals(someLastName, persisted.getLastName());
        assertEquals(someDisplayName, persisted.getDisplayName());
        assertEquals(somePhoneNumber, persisted.getPhoneNumber());
        assertEquals(someEmail, persisted.getEmail());
    }

    @Test
    void add_givenAnyContactToInsert_shouldReturnsContactWithNotBlankId() {
        var toInsert = mock(ContactToInsert.class);

        var persisted = repository.add(toInsert);

        assertNotNull(persisted.getId());
        assertNotEquals(0L, persisted.getId().getMostSignificantBits());
        assertNotEquals(0L, persisted.getId().getLeastSignificantBits());
    }

    @Test
    void getById__givenNonExistentId__shouldReturnsNotPresentOptional() {
        var contact = repository.getById(randomUUID());

        assertFalse(contact.isPresent());
    }

    @Test
    void getById__givenExistentId__shouldReturnsPresentOptional() {
        var toInsert = mock(ContactToInsert.class);
        var persisted = repository.add(toInsert);
        var existentId = persisted.getId();

        var contact = repository.getById(existentId);

        assertTrue(contact.isPresent());
    }

    @Test
    void getById__givenExistentIdAndEarlierSomeContactAdded__shouldReturnsPresentOptionalWithGivenContactData() {
        var toInsert = mock(ContactToInsert.class);
        var someFirstName = Faker.firstName();
        when(toInsert.getFirstName()).thenReturn(someFirstName);
        var someLastName = Faker.lastName();
        when(toInsert.getLastName()).thenReturn(someLastName);
        var someDisplayName = Faker.nickname();
        when(toInsert.getDisplayName()).thenReturn(someDisplayName);
        var somePhoneNumber = Faker.phoneNumber();
        when(toInsert.getPhoneNumber()).thenReturn(somePhoneNumber);
        var someEmail = Faker.email();
        when(toInsert.getEmail()).thenReturn(someEmail);

        var existentId = repository.add(toInsert).getId();

        var contact = repository.getById(existentId).get();

        assertEquals(someFirstName, contact.getFirstName());
        assertEquals(someLastName, contact.getLastName());
        assertEquals(someDisplayName, contact.getDisplayName());
        assertEquals(somePhoneNumber, contact.getPhoneNumber());
        assertEquals(someEmail, contact.getEmail());
    }

    @Test
    void getById__givenExistentIdAndEarlierAnyContactAdded__shouldReturnsPresentOptionalWithNotBlankId() {
        var toInsert = mock(ContactToInsert.class);

        var existentId = repository.add(toInsert).getId();

        var contact = repository.getById(existentId).get();

        assertNotEquals(0L, contact.getId().getMostSignificantBits());
        assertNotEquals(0L, contact.getId().getLeastSignificantBits());
    }
}
