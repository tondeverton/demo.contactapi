package com.tondeverton.demo.contactapi.repositories;

import jakarta.validation.constraints.NotBlank;

public interface ContactToInsert {
    @NotBlank
    String getFirstName();

    @NotBlank
    String getLastName();

    @NotBlank
    String getDisplayName();

    @NotBlank
    String getPhoneNumber();

    @NotBlank
    String getEmail();
}
