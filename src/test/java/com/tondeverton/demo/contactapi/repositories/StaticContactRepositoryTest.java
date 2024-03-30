package com.tondeverton.demo.contactapi.repositories;

import com.tondeverton.demo.contactapi.testutilities.Faker;
import com.tondeverton.demo.contactapi.testutilities.FakerFactory;
import com.tondeverton.demo.contactapi.utilities.StringSimilarityUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;

import static java.lang.System.*;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class StaticContactRepositoryTest {

    @MockBean
    private StringSimilarityUtil stringSimilarity;
    @Autowired
    private StaticContactRepository repository;

    @BeforeEach
    public void clearContactList() {
        StaticContactRepository.contacts.clear();
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
        var toInsert = FakerFactory.contactToInsert();

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
        var toInsert = FakerFactory.contactToInsert();
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
        var toInsert = FakerFactory.contactToInsert();

        var existentId = repository.add(toInsert).getId();

        var contact = repository.getById(existentId).get();

        assertNotEquals(0L, contact.getId().getMostSignificantBits());
        assertNotEquals(0L, contact.getId().getLeastSignificantBits());
    }

    @Test
    void getAll__shouldReturnsContactsWithTheSameDataFromStoredContacts() {
        var contactOne = FakerFactory.contactEntity();
        var contactTwo = FakerFactory.contactEntity();
        var contactThree = FakerFactory.contactEntity();
        StaticContactRepository.contacts.addAll(List.of(contactOne, contactTwo, contactThree));

        var contacts = repository.getAll().toArray();

        assertEquals(3, contacts.length);
        assertThat(contacts[0]).usingRecursiveComparison().isEqualTo(contactOne);
        assertThat(contacts[1]).usingRecursiveComparison().isEqualTo(contactTwo);
        assertThat(contacts[2]).usingRecursiveComparison().isEqualTo(contactThree);
    }

    @Test
    void getAll__shouldReturnsAClonedListWithClonedItems() {
        var contactOne = FakerFactory.contactEntity();
        var contactTwo = FakerFactory.contactEntity();
        var contactThree = FakerFactory.contactEntity();
        StaticContactRepository.contacts.addAll(List.of(contactOne, contactTwo, contactThree));

        var contactsFromRepository = repository.getAll();

        assertNotEquals(identityHashCode(StaticContactRepository.contacts), identityHashCode(contactsFromRepository));

        var contactsAsArray = StaticContactRepository.contacts.toArray();
        var contactsFromRepositoryAsArray = contactsFromRepository.toArray();

        assertNotEquals(identityHashCode(contactsAsArray[0]), identityHashCode(contactsFromRepositoryAsArray[0]));
        assertNotEquals(identityHashCode(contactsAsArray[1]), identityHashCode(contactsFromRepositoryAsArray[1]));
        assertNotEquals(identityHashCode(contactsAsArray[2]), identityHashCode(contactsFromRepositoryAsArray[2]));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    void getAllBySearch__givenAnySearchAndAnyMinPercentSimilarity__shouldReturnsOnlyOneWithPercentGreaterThanMinOfThreeExistentContacts(
            int contactToReturn
    ) {
        var contactOne = repository.add(FakerFactory.contactToInsert());
        var contactTwo = repository.add(FakerFactory.contactToInsert());
        var contactThree = repository.add(FakerFactory.contactToInsert());

        var anySearch = Faker.word();
        var anyMinPercentSimilarity = (double) Faker.intBetween(5, 95);

        var greaterThanPercentSimilarity = anyMinPercentSimilarity + 1;
        var lowerThanPercentSimilarity = anyMinPercentSimilarity - 1;
        when(stringSimilarity.percentageBetween(anyString(), anyString()))
                .thenReturn(
                        contactToReturn == 1 ? greaterThanPercentSimilarity : lowerThanPercentSimilarity,
                        contactToReturn == 2 ? greaterThanPercentSimilarity : lowerThanPercentSimilarity,
                        contactToReturn == 3 ? greaterThanPercentSimilarity : lowerThanPercentSimilarity
                );

        var contacts = repository.getAllBySearch(anySearch, anyMinPercentSimilarity);

        assertEquals(1, contacts.size());

        var contactId = contacts.stream().findFirst().get().getId();
        switch (contactToReturn) {
            case 1:
                assertEquals(contactOne.getId(), contactId);
                break;
            case 2:
                assertEquals(contactTwo.getId(), contactId);
                break;
            case 3:
                assertEquals(contactThree.getId(), contactId);
                break;
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    void getAllBySearch__givenAnySearchAndAnyMinPercentSimilarity__shouldReturnsOnlyOneWithPercentEqualToMinOfThreeExistentContacts(
            int contactToReturn
    ) {
        var contactOne = repository.add(FakerFactory.contactToInsert());
        var contactTwo = repository.add(FakerFactory.contactToInsert());
        var contactThree = repository.add(FakerFactory.contactToInsert());

        var anySearch = Faker.word();
        var anyMinPercentSimilarity = (double) Faker.intBetween(5, 95);

        var lowerThanPercentSimilarity = anyMinPercentSimilarity - 1;
        when(stringSimilarity.percentageBetween(anyString(), anyString()))
                .thenReturn(
                        contactToReturn == 1 ? anyMinPercentSimilarity : lowerThanPercentSimilarity,
                        contactToReturn == 2 ? anyMinPercentSimilarity : lowerThanPercentSimilarity,
                        contactToReturn == 3 ? anyMinPercentSimilarity : lowerThanPercentSimilarity
                );

        var contacts = repository.getAllBySearch(anySearch, anyMinPercentSimilarity);

        assertEquals(1, contacts.size());

        var contactId = contacts.stream().findFirst().get().getId();
        switch (contactToReturn) {
            case 1:
                assertEquals(contactOne.getId(), contactId);
                break;
            case 2:
                assertEquals(contactTwo.getId(), contactId);
                break;
            case 3:
                assertEquals(contactThree.getId(), contactId);
                break;
        }
    }

    @Test
    void getAllBySearch__givenAnySearchAndAnyMinPercentSimilarity__shouldProvidesForStringSimilarityAllDataFromContactToInsert() {
        var contact = repository.add(FakerFactory.contactToInsert());
        var expectedContactProperties = contact.getFirstName()
                .concat(" ").concat(contact.getLastName())
                .concat(" ").concat(contact.getDisplayName())
                .concat(" ").concat(contact.getPhoneNumber())
                .concat(" ").concat(contact.getEmail());

        var anySearch = Faker.word();
        var anyMinPercentSimilarity = (double) Faker.intBetween(5, 95);

        var contactPropertiesCaptor = ArgumentCaptor.forClass(String.class);

        when(stringSimilarity.percentageBetween(anyString(), contactPropertiesCaptor.capture())).thenReturn(anyMinPercentSimilarity);
        repository.getAllBySearch(anySearch, anyMinPercentSimilarity);

        assertEquals(expectedContactProperties, contactPropertiesCaptor.getValue());
    }

    @Test
    void getAllBySearch__givenAnySearchAndAnyMinPercentSimilarity__shouldReturnsAClonedListWithClonedItems() {
        var contactOne = FakerFactory.contactEntity();
        var contactTwo = FakerFactory.contactEntity();
        var contactThree = FakerFactory.contactEntity();
        StaticContactRepository.contacts.addAll(List.of(contactOne, contactTwo, contactThree));

        var anyMinPercentSimilarity = (double) Faker.intBetween(5, 95);
        when(stringSimilarity.percentageBetween(anyString(), anyString())).thenReturn(anyMinPercentSimilarity);
        var contactsFromRepository = repository.getAllBySearch(Faker.word(), anyMinPercentSimilarity);

        assertNotEquals(identityHashCode(StaticContactRepository.contacts), identityHashCode(contactsFromRepository));

        var contactsAsArray = StaticContactRepository.contacts.toArray();
        var contactsFromRepositoryAsArray = contactsFromRepository.toArray();

        assertNotEquals(identityHashCode(contactsAsArray[0]), identityHashCode(contactsFromRepositoryAsArray[0]));
        assertNotEquals(identityHashCode(contactsAsArray[1]), identityHashCode(contactsFromRepositoryAsArray[1]));
        assertNotEquals(identityHashCode(contactsAsArray[2]), identityHashCode(contactsFromRepositoryAsArray[2]));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "firstName",
            "lastName",
            "displayName",
            "phoneNumber",
            "email"
    })
    void add__givenSomeContactToInsertWithBlankAttribute__shouldThrowsExceptionSpecificByBlankAttribute(String blankAttribute) {
        var contact = FakerFactory.contactToInsert();
        switch (blankAttribute) {
            case "firstName":
                when(contact.getFirstName()).thenReturn("");
                break;
            case "lastName":
                when(contact.getLastName()).thenReturn("");
                break;
            case "displayName":
                when(contact.getDisplayName()).thenReturn("");
                break;
            case "phoneNumber":
                when(contact.getPhoneNumber()).thenReturn("");
                break;
            case "email":
                when(contact.getEmail()).thenReturn("");
                break;
        }

        var exception = assertThrows(Exception.class, () -> repository.add(contact));
        assertTrue(exception.getMessage().contains(blankAttribute));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "firstName",
            "lastName",
            "displayName",
            "phoneNumber",
            "email"
    })
    void update__givenAnyIdAndAndSomeContactToInsertWithBlankAttribute__shouldThrowsExceptionSpecificByBlankAttribute(String blankAttribute) {
        var contact = FakerFactory.contactToInsert();
        switch (blankAttribute) {
            case "firstName":
                when(contact.getFirstName()).thenReturn("");
                break;
            case "lastName":
                when(contact.getLastName()).thenReturn("");
                break;
            case "displayName":
                when(contact.getDisplayName()).thenReturn("");
                break;
            case "phoneNumber":
                when(contact.getPhoneNumber()).thenReturn("");
                break;
            case "email":
                when(contact.getEmail()).thenReturn("");
                break;
        }

        var exception = assertThrows(Exception.class, () -> repository.update(randomUUID(), contact));
        assertTrue(exception.getMessage().contains(blankAttribute));
    }
}
