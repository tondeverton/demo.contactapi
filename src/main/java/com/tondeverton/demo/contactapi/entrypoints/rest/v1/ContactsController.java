package com.tondeverton.demo.contactapi.entrypoints.rest.v1;

import com.tondeverton.demo.contactapi.entrypoints.rest.v1.reqsress.SaveContactRequest;
import com.tondeverton.demo.contactapi.exceptions.PreconditionException;
import com.tondeverton.demo.contactapi.repositories.Contact;
import com.tondeverton.demo.contactapi.usecases.ContactsUseCase;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/v1/contacts")
@Validated
public class ContactsController {

    private final ContactsUseCase contactsUseCase;

    public ContactsController(ContactsUseCase contactsUseCase) {
        this.contactsUseCase = contactsUseCase;
    }

    @PostMapping(produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    public Contact save(@Valid @NotNull @RequestBody SaveContactRequest request) {
        return contactsUseCase.save(request);
    }

    @PutMapping(value = "{id}", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    public ResponseEntity<Contact> update(@NotNull @PathVariable UUID id, @Valid @NotNull @RequestBody SaveContactRequest request) {
        Optional<Contact> optional = contactsUseCase.update(id, request);
        if (optional.isPresent())
            return ResponseEntity.of(optional);

        throw new PreconditionException("Contact not found");
    }
}
