package com.tondeverton.demo.contactapi.repositories;

import com.tondeverton.demo.contactapi.validators.PhoneNumber;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public interface ContactToSave {
    @NotBlank
    @Length(max = 30)
    String getFirstName();

    @NotBlank
    @Length(max = 30)
    String getLastName();

    @NotBlank
    @Length(max = 15)
    String getDisplayName();

    @NotBlank
    @Length(max = 20)
    @PhoneNumber
    String getPhoneNumber();

    @NotBlank
    @Email
    String getEmail();
}
