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
    public Contact update(@NotNull @PathVariable UUID id, @Valid @NotNull @RequestBody SaveContactRequest request) {
        return contactsUseCase.update(id, request).orElseThrow(() -> new PreconditionException("Contact not found"));
    }

    @GetMapping(value = "{id}", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    public ResponseEntity<Contact> get(@NotNull @PathVariable UUID id) {
        return contactsUseCase.get(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(NO_CONTENT).build());
    }
}
