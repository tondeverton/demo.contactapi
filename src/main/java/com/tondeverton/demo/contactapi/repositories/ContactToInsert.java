package com.tondeverton.demo.contactapi.repositories;

import com.tondeverton.demo.contactapi.validators.PhoneNumber;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public interface ContactToInsert {
    @NotBlank
    @Length(max = 30)
    String firstName();

    @NotBlank
    @Length(max = 30)
    String lastName();

    @NotBlank
    @Length(max = 15)
    String displayName();

    @NotBlank
    @Length(max = 20)
    @PhoneNumber
    String phoneNumber();

    @NotBlank
    @Email
    String email();
}
