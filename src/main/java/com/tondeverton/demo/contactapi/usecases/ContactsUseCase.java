package com.tondeverton.demo.contactapi.usecases;

import com.tondeverton.demo.contactapi.repositories.Contact;
import com.tondeverton.demo.contactapi.repositories.ContactRepository;
import com.tondeverton.demo.contactapi.repositories.ContactToSave;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class ContactsUseCase {

    private final ContactRepository contactRepository;

    public ContactsUseCase(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public Contact save(@NotNull @Valid ContactToSave dto) {
        return contactRepository.add(dto);
    }
}
