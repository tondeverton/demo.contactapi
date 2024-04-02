package com.tondeverton.demo.contactapi.repositories;

import com.tondeverton.demo.contactapi.utilities.StringSimilarityLevenshtein;
import com.tondeverton.demo.contactapi.utilities.StringSimilarityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import static java.util.UUID.randomUUID;

@Repository
public class StaticContactRepository implements ContactRepository {

    protected static final Collection<ContactEntity> contacts = new ArrayList<>();

    private final StringSimilarityUtil stringSimilarity;

    public StaticContactRepository(
            @Autowired(required = false) StringSimilarityUtil stringSimilarity
    ) {
        this.stringSimilarity = stringSimilarity == null ? new StringSimilarityLevenshtein() : stringSimilarity;
    }

    @Override
    public Contact add(ContactToInsert toInsert) {
        var contact = new ContactEntity();
        contact.setFirstName(toInsert.firstName());
        contact.setLastName(toInsert.lastName());
        contact.setDisplayName(toInsert.displayName());
        contact.setPhoneNumber(toInsert.phoneNumber());
        contact.setEmail(toInsert.email());

        contact.setId(randomUUID());

        contacts.add(contact);

        return contact.clone();
    }

    @Override
    public Optional<Contact> getById(UUID id) {
        return contacts.stream().filter(c -> id.equals(c.id())).findFirst().map(c -> c);
    }

    @Override
    public Collection<Contact> getAll() {
        return contacts.stream().map(ContactEntity::clone).map(c -> (Contact) c).toList();
    }

    @Override
    public Collection<Contact> getAllBySearch(String search, double minPercentSimilarity) {
        return contacts.stream().filter(c -> {
                    var contactProperties = c.firstName()
                            .concat(" ").concat(c.lastName())
                            .concat(" ").concat(c.displayName())
                            .concat(" ").concat(c.phoneNumber())
                            .concat(" ").concat(c.email());

                    return stringSimilarity.percentageBetween(search, contactProperties) >= minPercentSimilarity;
                })
                .map(ContactEntity::clone)
                .map(c -> (Contact) c)
                .toList();
    }

    @Override
    public Optional<Contact> update(UUID id, ContactToInsert toInsert) {
        var contact = contacts.stream().filter(c -> c.id().equals(id)).findFirst();
        contact.ifPresent(c -> {
            c.setFirstName(toInsert.firstName());
            c.setLastName(toInsert.lastName());
            c.setDisplayName(toInsert.displayName());
            c.setPhoneNumber(toInsert.phoneNumber());
            c.setEmail(toInsert.email());
        });
        return contact.map(ContactEntity::clone);
    }

    @Override
    public boolean deleteById(UUID id) {
        return contacts.removeIf(c -> id.equals(c.id()));
    }
}
