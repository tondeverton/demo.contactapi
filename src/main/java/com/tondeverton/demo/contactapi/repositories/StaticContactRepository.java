package com.tondeverton.demo.contactapi.repositories;

import com.tondeverton.demo.contactapi.utilities.StringSimilarityLevenshtein;
import com.tondeverton.demo.contactapi.utilities.StringSimilarityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Repository
public class StaticContactRepository implements ContactRepository {

    private static Collection<ContactEntity> contacts;

    private final StringSimilarityUtil stringSimilarity;

    public StaticContactRepository(
            @Autowired(required = false) StringSimilarityUtil stringSimilarity
    ) {
        this.stringSimilarity = stringSimilarity == null ? new StringSimilarityLevenshtein() : stringSimilarity;

        contacts = new ArrayList<>();
    }

    @Override
    public Contact add(ContactToInsert toInsert) {
        var contact = new ContactEntity();
        contact.setFirstName(toInsert.getFirstName());
        contact.setLastName(toInsert.getLastName());
        contact.setDisplayName(toInsert.getDisplayName());
        contact.setPhoneNumber(toInsert.getPhoneNumber());
        contact.setEmail(toInsert.getEmail());

        contact.setId(UUID.randomUUID());

        return contact;
    }

    @Override
    public Optional<Contact> getById(UUID id) {
        return contacts.stream().filter(c -> id.equals(c.getId())).findFirst().map(c -> c);
    }

    @Override
    public Collection<Contact> getAllBySearch(String search) {
        return contacts.stream().filter(c -> {
            var contactProperties = c.getFirstName()
                    .concat(" ").concat(c.getLastName())
                    .concat(" ").concat(c.getDisplayName())
                    .concat(" ").concat(c.getPhoneNumber())
                    .concat(" ").concat(c.getEmail());

            return stringSimilarity.percentageBetween(search, contactProperties) > 30;
        }).map(c -> (Contact) c).toList();
    }

    @Override
    public Optional<Contact> update(UUID id, ContactToInsert toInsert) {
        var contact = contacts.stream().findFirst();
        contact.ifPresent(c -> {
            c.setFirstName(toInsert.getFirstName());
            c.setLastName(toInsert.getLastName());
            c.setDisplayName(toInsert.getDisplayName());
            c.setPhoneNumber(toInsert.getPhoneNumber());
            c.setEmail(toInsert.getEmail());
        });
        return contact.map(c -> c);
    }

    @Override
    public boolean deleteById(UUID id) {
        return contacts.removeIf(c -> id.equals(c.getId()));
    }
}
