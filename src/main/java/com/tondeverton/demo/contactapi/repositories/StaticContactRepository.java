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
    public Contact add(ContactToSave toSave) {
        var contact = new ContactEntity();
        contact.setFirstName(toSave.getFirstName());
        contact.setLastName(toSave.getLastName());
        contact.setDisplayName(toSave.getDisplayName());
        contact.setPhoneNumber(toSave.getPhoneNumber());
        contact.setEmail(toSave.getEmail());

        contact.setId(randomUUID());

        contacts.add(contact);

        return contact.clone();
    }

    @Override
    public Optional<Contact> getById(UUID id) {
        return contacts.stream().filter(c -> id.equals(c.getId())).findFirst().map(c -> c);
    }

    @Override
    public Collection<Contact> getAll() {
        return contacts.stream().map(ContactEntity::clone).map(c -> (Contact) c).toList();
    }

    @Override
    public Collection<Contact> getAllBySearch(String search, double minPercentSimilarity) {
        return contacts.stream().filter(c -> {
                    var contactProperties = c.getFirstName()
                            .concat(" ").concat(c.getLastName())
                            .concat(" ").concat(c.getDisplayName())
                            .concat(" ").concat(c.getPhoneNumber())
                            .concat(" ").concat(c.getEmail());

                    return stringSimilarity.percentageBetween(search, contactProperties) >= minPercentSimilarity;
                })
                .map(ContactEntity::clone)
                .map(c -> (Contact) c)
                .toList();
    }

    @Override
    public Optional<Contact> update(UUID id, ContactToSave toSave) {
        var contact = contacts.stream().filter(c -> c.getId().equals(id)).findFirst();
        contact.ifPresent(c -> {
            c.setFirstName(toSave.getFirstName());
            c.setLastName(toSave.getLastName());
            c.setDisplayName(toSave.getDisplayName());
            c.setPhoneNumber(toSave.getPhoneNumber());
            c.setEmail(toSave.getEmail());
        });
        return contact.map(ContactEntity::clone);
    }

    @Override
    public boolean deleteById(UUID id) {
        return contacts.removeIf(c -> id.equals(c.getId()));
    }
}
