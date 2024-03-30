package com.tondeverton.demo.contactapi.repositories;

import jakarta.validation.Valid;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Validated

public interface ContactRepository {
    Contact add(@Valid ContactToInsert contact);

    Optional<Contact> getById(UUID id);

    Collection<Contact> getAll();

    Collection<Contact> getAllBySearch(String search, double minPercentSimilarity);

    Optional<Contact> update(UUID id, @Valid ContactToInsert contact);

    boolean deleteById(UUID id);
}
