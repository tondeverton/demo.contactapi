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

import java.util.List;

import static java.lang.System.identityHashCode;
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
    void add_givenSomeContactToSave_shouldReturnsContactWithTheSamePropertiesFromInput() {
        var toSave = mock(ContactToSave.class);

        var someFirstName = Faker.firstName();
        when(toSave.getFirstName()).thenReturn(someFirstName);
        var someLastName = Faker.lastName();
        when(toSave.getLastName()).thenReturn(someLastName);
        var someDisplayName = Faker.nickname();
        when(toSave.getDisplayName()).thenReturn(someDisplayName);
        var somePhoneNumber = Faker.phoneNumber();
        when(toSave.getPhoneNumber()).thenReturn(somePhoneNumber);
        var someEmail = Faker.email();
        when(toSave.getEmail()).thenReturn(someEmail);

        var persisted = repository.add(toSave);

        assertEquals(someFirstName, persisted.getFirstName());
        assertEquals(someLastName, persisted.getLastName());
        assertEquals(someDisplayName, persisted.getDisplayName());
        assertEquals(somePhoneNumber, persisted.getPhoneNumber());
        assertEquals(someEmail, persisted.getEmail());
    }

    @Test
    void add_givenAnyContactToSave_shouldReturnsContactWithNotBlankId() {
        var toSave = FakerFactory.contactToSave();

        var persisted = repository.add(toSave);

        assertNotNull(persisted.getIdentifier());
        assertNotEquals(0L, persisted.getIdentifier().getMostSignificantBits());
        assertNotEquals(0L, persisted.getIdentifier().getLeastSignificantBits());
    }

    @Test
    void add__givenAnyContactToSave_shouldStoresOnlyOneContactAndReturnsClonedContact() {
        var toSave = FakerFactory.contactToSave();

        var persisted = repository.add(toSave);

        assertEquals(1, StaticContactRepository.contacts.size());
        assertNotEquals(identityHashCode(StaticContactRepository.contacts.toArray()[0]), identityHashCode(persisted));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "firstName",
            "lastName",
            "displayName",
            "phoneNumber",
            "email"
    })
    void add__givenSomeContactToSaveWithBlankAttribute__shouldThrowsExceptionSpecificByBlankAttribute(String blankAttribute) {
        var contact = spy(FakerFactory.contactToSave());
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

    @Test
    void add_givenNullContact__shouldThrowsExceptionWithMessageOfNull() {
        var exception = assertThrows(Exception.class, () -> repository.add(null));
        assertThat(exception.getMessage()).contains("must not be null");
    }

    @Test
    void getById__givenNonExistentIdentifier__shouldReturnsNotPresentOptional() {
        var contact = repository.getByIdentifier(randomUUID());

        assertFalse(contact.isPresent());
    }

    @Test
    void getById__givenExistentIdentifier__shouldReturnsPresentOptional() {
        var toSave = FakerFactory.contactToSave();
        var persisted = repository.add(toSave);
        var existentId = persisted.getIdentifier();

        var contact = repository.getByIdentifier(existentId);

        assertTrue(contact.isPresent());
    }

    @Test
    void getById__givenExistentIdentifierAndEarlierSomeContactAdded__shouldReturnsPresentOptionalWithGivenContactData() {
        var toSave = mock(ContactToSave.class);
        var someFirstName = Faker.firstName();
        when(toSave.getFirstName()).thenReturn(someFirstName);
        var someLastName = Faker.lastName();
        when(toSave.getLastName()).thenReturn(someLastName);
        var someDisplayName = Faker.nickname();
        when(toSave.getDisplayName()).thenReturn(someDisplayName);
        var somePhoneNumber = Faker.phoneNumber();
        when(toSave.getPhoneNumber()).thenReturn(somePhoneNumber);
        var someEmail = Faker.email();
        when(toSave.getEmail()).thenReturn(someEmail);

        var existentId = repository.add(toSave).getIdentifier();

        var contact = repository.getByIdentifier(existentId).get();

        assertEquals(someFirstName, contact.getFirstName());
        assertEquals(someLastName, contact.getLastName());
        assertEquals(someDisplayName, contact.getDisplayName());
        assertEquals(somePhoneNumber, contact.getPhoneNumber());
        assertEquals(someEmail, contact.getEmail());
    }

    @Test
    void getById__givenExistentIdAndEarlierAnyContactAdded__shouldReturnsPresentOptionalWithNotBlankIdentifier() {
        var toSave = FakerFactory.contactToSave();

        var existentId = repository.add(toSave).getIdentifier();

        var contact = repository.getByIdentifier(existentId).get();

        assertNotEquals(0L, contact.getIdentifier().getMostSignificantBits());
        assertNotEquals(0L, contact.getIdentifier().getLeastSignificantBits());
    }

    @Test
    void getById_givenNullIdentifier__shouldThrowsExceptionWithMessageOfNull() {
        var exception = assertThrows(Exception.class, () -> repository.getByIdentifier(null));
        assertThat(exception.getMessage()).contains("must not be null");
    }

    @Test
    void getAll__shouldReturnsContactsWithTheSameDataFromStoredContacts() {
        var contactOne = FakerFactory.contactEntity();
        var contactTwo = FakerFactory.contactEntity();
        var contactThree = FakerFactory.contactEntity();
        StaticContactRepository.contacts.addAll(List.of(contactOne, contactTwo, contactThree));

        var contacts = repository.getAll().get().toArray();

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
        var contactsFromRepositoryAsArray = contactsFromRepository.get().toArray();

        assertNotEquals(identityHashCode(contactsAsArray[0]), identityHashCode(contactsFromRepositoryAsArray[0]));
        assertNotEquals(identityHashCode(contactsAsArray[1]), identityHashCode(contactsFromRepositoryAsArray[1]));
        assertNotEquals(identityHashCode(contactsAsArray[2]), identityHashCode(contactsFromRepositoryAsArray[2]));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    void getAll__givenAnySearchAndAnyMinPercentSimilarity__shouldReturnsOnlyOneWithPercentGreaterThanMinOfThreeExistentContacts(
            int contactToReturn
    ) {
        var contactOne = repository.add(FakerFactory.contactToSave());
        var contactTwo = repository.add(FakerFactory.contactToSave());
        var contactThree = repository.add(FakerFactory.contactToSave());

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

        var contacts = repository.getAll(anySearch, anyMinPercentSimilarity);

        assertEquals(1, contacts.getTotalElements());

        var contactId = contacts.stream().findFirst().get().getIdentifier();
        switch (contactToReturn) {
            case 1:
                assertEquals(contactOne.getIdentifier(), contactId);
                break;
            case 2:
                assertEquals(contactTwo.getIdentifier(), contactId);
                break;
            case 3:
                assertEquals(contactThree.getIdentifier(), contactId);
                break;
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    void getAll__givenAnySearchAndAnyMinPercentSimilarity__shouldReturnsOnlyOneWithPercentEqualToMinOfThreeExistentContacts(
            int contactToReturn
    ) {
        var contactOne = repository.add(FakerFactory.contactToSave());
        var contactTwo = repository.add(FakerFactory.contactToSave());
        var contactThree = repository.add(FakerFactory.contactToSave());

        var anySearch = Faker.word();
        var anyMinPercentSimilarity = (double) Faker.intBetween(5, 95);

        var lowerThanPercentSimilarity = anyMinPercentSimilarity - 1;
        when(stringSimilarity.percentageBetween(anyString(), anyString()))
                .thenReturn(
                        contactToReturn == 1 ? anyMinPercentSimilarity : lowerThanPercentSimilarity,
                        contactToReturn == 2 ? anyMinPercentSimilarity : lowerThanPercentSimilarity,
                        contactToReturn == 3 ? anyMinPercentSimilarity : lowerThanPercentSimilarity
                );

        var contacts = repository.getAll(anySearch, anyMinPercentSimilarity);

        assertEquals(1, contacts.getTotalElements());

        var contactId = contacts.stream().findFirst().get().getIdentifier();
        switch (contactToReturn) {
            case 1:
                assertEquals(contactOne.getIdentifier(), contactId);
                break;
            case 2:
                assertEquals(contactTwo.getIdentifier(), contactId);
                break;
            case 3:
                assertEquals(contactThree.getIdentifier(), contactId);
                break;
        }
    }

    @Test
    void getAll__givenAnySearchAndAnyMinPercentSimilarity__shouldProvidesForStringSimilarityAllDataFromContactToSave() {
        var contact = repository.add(FakerFactory.contactToSave());
        var expectedContactProperties = contact.getFirstName()
                .concat(" ").concat(contact.getLastName())
                .concat(" ").concat(contact.getDisplayName())
                .concat(" ").concat(contact.getPhoneNumber())
                .concat(" ").concat(contact.getEmail());

        var anySearch = Faker.word();
        var anyMinPercentSimilarity = (double) Faker.intBetween(5, 95);

        var contactPropertiesCaptor = ArgumentCaptor.forClass(String.class);

        when(stringSimilarity.percentageBetween(anyString(), contactPropertiesCaptor.capture())).thenReturn(anyMinPercentSimilarity);
        repository.getAll(anySearch, anyMinPercentSimilarity);

        assertEquals(expectedContactProperties, contactPropertiesCaptor.getValue());
    }

    @Test
    void getAll__givenAnySearchAndAnyMinPercentSimilarity__shouldReturnsAClonedListWithClonedItems() {
        var contactOne = FakerFactory.contactEntity();
        var contactTwo = FakerFactory.contactEntity();
        var contactThree = FakerFactory.contactEntity();
        StaticContactRepository.contacts.addAll(List.of(contactOne, contactTwo, contactThree));

        var anyMinPercentSimilarity = (double) Faker.intBetween(5, 95);
        when(stringSimilarity.percentageBetween(anyString(), anyString())).thenReturn(anyMinPercentSimilarity);
        var contactsFromRepository = repository.getAll(Faker.word(), anyMinPercentSimilarity);

        assertNotEquals(identityHashCode(StaticContactRepository.contacts), identityHashCode(contactsFromRepository));

        var contactsAsArray = StaticContactRepository.contacts.toArray();
        var contactsFromRepositoryAsArray = contactsFromRepository.get().toArray();

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
    void update__givenAnyIdAndAndSomeContactToSaveWithBlankAttribute__shouldThrowsExceptionSpecificByBlankAttribute(String blankAttribute) {
        var contact = spy(FakerFactory.contactToSave());
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

    @Test
    void update__givenAnyIdAndAnyContactToSave_shouldNotAddAnotherItemInTheListAndReturnsAClonedContact() {
        var contact = FakerFactory.contactEntity();
        StaticContactRepository.contacts.add(contact);

        var updated = repository.update(contact.getIdentifier(), FakerFactory.contactToSave()).get();

        assertEquals(1, StaticContactRepository.contacts.size());
        assertNotEquals(identityHashCode(StaticContactRepository.contacts.toArray()[0]), identityHashCode(updated));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2})
    void update__givenSomeExistentIdAndSomeContactToSave_shouldUpdateOnlyTheMatchedContactGivenThreeStored(int contactToUpdate) {
        var contactOne = FakerFactory.contactEntity();
        var contactTwo = FakerFactory.contactEntity();
        var contactThree = FakerFactory.contactEntity();
        StaticContactRepository.contacts.addAll(List.of(contactOne, contactTwo, contactThree));

        var updated = repository.update(
                contactToUpdate == 0 ? contactOne.getIdentifier() :
                        contactToUpdate == 1 ? contactTwo.getIdentifier() : contactThree.getIdentifier(),
                FakerFactory.contactToSave()
        ).get();

        var contactsAsArray = StaticContactRepository.contacts.toArray();

        assertThat(contactsAsArray[0]).usingRecursiveComparison().isEqualTo(contactToUpdate == 0 ? updated : contactOne);
        assertThat(contactsAsArray[1]).usingRecursiveComparison().isEqualTo(contactToUpdate == 1 ? updated : contactTwo);
        assertThat(contactsAsArray[2]).usingRecursiveComparison().isEqualTo(contactToUpdate == 2 ? updated : contactThree);
    }

    @Test
    void update_givenNullId__shouldThrowsExceptionWithMessageOfNull() {
        var exception = assertThrows(Exception.class, () -> repository.update(null, FakerFactory.contactToSave()));
        assertThat(exception.getMessage()).contains("must not be null");
    }

    @Test
    void update__givenNonExistentIdAndAnyContact__shouldReturnsNotPresentOptionalAndNotUpdateAnyContactsData() {
        var contactOne = FakerFactory.contactEntity();
        var contactTwo = FakerFactory.contactEntity();
        var contactThree = FakerFactory.contactEntity();
        StaticContactRepository.contacts.addAll(List.of(contactOne, contactTwo, contactThree));

        var contact = repository.update(randomUUID(), FakerFactory.contactToSave());

        assertFalse(contact.isPresent());
        assertThat(StaticContactRepository.contacts).hasSize(3);

        var contactsAsArray = StaticContactRepository.contacts.toArray();

        assertThat(contactsAsArray[0]).usingRecursiveComparison().isEqualTo(contactOne);
        assertThat(contactsAsArray[1]).usingRecursiveComparison().isEqualTo(contactTwo);
        assertThat(contactsAsArray[2]).usingRecursiveComparison().isEqualTo(contactThree);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2})
    void delete__givenExistentId__shouldReturnsTrueAndDeleteOnlyTheMatchedContactGivenThreeStore(int contactToDelete) {
        var contactOne = FakerFactory.contactEntity();
        var contactTwo = FakerFactory.contactEntity();
        var contactThree = FakerFactory.contactEntity();
        var contacts = List.of(contactOne, contactTwo, contactThree);
        StaticContactRepository.contacts.addAll(contacts);

        var contactIdToDelete = contactToDelete == 0 ? contactOne.getIdentifier() :
                contactToDelete == 1 ? contactTwo.getIdentifier() :
                        contactThree.getIdentifier();

        var expectedStoredContactsAfterDelete = contacts.stream()
                .map(ContactEntity::getIdentifier).filter(id -> !id.equals(contactIdToDelete)).toList();

        var wasDeleted = repository.deleteById(contactIdToDelete);

        assertTrue(wasDeleted);
        assertEquals(2, StaticContactRepository.contacts.size());
        assertThat(StaticContactRepository.contacts.stream().map(ContactEntity::getIdentifier))
                .containsAll(expectedStoredContactsAfterDelete);
    }

    @Test
    void delete_givenNullId__shouldThrowsExceptionWithMessageOfNull() {
        var exception = assertThrows(Exception.class, () -> repository.deleteById(null));
        assertThat(exception.getMessage()).contains("must not be null");
    }
}
