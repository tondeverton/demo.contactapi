package com.tondeverton.demo.contactapi.repositories;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface ContactRepository {
    Contact add(ContactToInsert contact);
    Optional<Contact> getById(UUID id);
    Collection<Contact> getAllBySearch(String search, double minPercentSimilarity);
    Optional<Contact> update(UUID id, ContactToInsert contact);
    boolean deleteById(UUID id);
}
