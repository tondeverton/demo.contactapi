package com.tondeverton.demo.contactapi.usecases;

import com.tondeverton.demo.contactapi.entrypoints.rest.v1.reqsress.SaveContactRequest;
import com.tondeverton.demo.contactapi.repositories.Contact;
import com.tondeverton.demo.contactapi.repositories.ContactRepository;
import com.tondeverton.demo.contactapi.repositories.ContactToSave;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;
import java.util.UUID;

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

    public Optional<Contact> update(@NotNull UUID id, @NotNull @Valid ContactToSave request) {
        return contactRepository.update(id, request);
    }

    public Optional<Contact> get(@NotNull UUID id) {
        return contactRepository.getById(id);
    }

    public Page<Contact> getAll(@Length(max = 50) String search, @Max(30) int page) {
        return null;
    }

    public Optional<Boolean> delete(@NotNull UUID id) {
        return Optional.of(contactRepository.deleteById(id));
    }
}
