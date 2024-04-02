package com.tondeverton.demo.contactapi.repositories;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Validated
public interface ContactRepository {
    Contact add(@NotNull @Valid ContactToSave contact);

    Optional<Contact> getById(@NotNull UUID id);

    Collection<Contact> getAll();

    Collection<Contact> getAllBySearch(String search, double minPercentSimilarity);

    Optional<Contact> update(@NotNull UUID id, @Valid ContactToSave contact);

    boolean deleteById(@NotNull UUID id);
}
