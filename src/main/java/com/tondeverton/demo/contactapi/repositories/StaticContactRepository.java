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

    private static Collection<Contact> contacts;

    private StringSimilarityUtil stringSimilarity;

    public StaticContactRepository(
            @Autowired(required = false) StringSimilarityUtil stringSimilarity
    ) {
        this.stringSimilarity = stringSimilarity == null ? new StringSimilarityLevenshtein() : stringSimilarity;

        contacts = new ArrayList<>();
    }

    @Override
    public Contact add(ContactToInsert contact) {
        return null;
    }

    @Override
    public Optional<Contact> getById(UUID id) {
        return contacts.stream().toList().stream().filter(c -> id.equals(c.getId())).findFirst();
    }

    @Override
    public Collection<Contact> getAllBySearch(String search) {
        return contacts.stream().filter(c -> {
            var contactProperties = "".concat(c.firstName())
                    .concat(" ").concat(c.lastName())
                    .concat(" ").concat(c.displayName())
                    .concat(" ").concat(c.phoneNumber())
                    .concat(" ").concat(c.email());

            return stringSimilarity.percentageBetween(search, contactProperties) > 30;
        }).toList();
    }

    @Override
    public Optional<Contact> update(UUID id, ContactToInsert contact) {
        return null;
    }

    @Override
    public void deleteById(UUID id) {

    }
}
