package com.tondeverton.demo.contactapi.repositories;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;
import java.util.UUID;

@Validated
public interface ContactRepository {
    Contact add(@NotNull @Valid ContactToSave contact);

    Optional<Contact> getById(@NotNull UUID id);

    Page<Contact> getAll();

    Page<Contact> getAll(@NotBlank String search, @Min(0) @Max(100) double minPercentSimilarity);

    Page<Contact> getAll(@Min(1) @Max(30) int page, int pageSize);

    Page<Contact> getAll(
            @Min(1) @Max(30) int page,
            int pageSize,
            @NotBlank String search,
            @Min(0) @Max(100) double minPercentSimilarity
    );

    Optional<Contact> update(@NotNull UUID id, @Valid ContactToSave contact);

    boolean deleteById(@NotNull UUID id);
}
