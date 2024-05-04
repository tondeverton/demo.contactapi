package com.tondeverton.demo.contactapi.entrypoints.rest.v1;

import com.tondeverton.demo.contactapi.entrypoints.rest.v1.reqsress.GetAllContactsResponse;
import com.tondeverton.demo.contactapi.entrypoints.rest.v1.reqsress.SaveContactRequest;
import com.tondeverton.demo.contactapi.exceptions.ContactNotFoundException;
import com.tondeverton.demo.contactapi.repositories.Contact;
import com.tondeverton.demo.contactapi.usecases.ContactsUseCase;
import com.tondeverton.demo.contactapi.validators.ContactsSearchPageSize;
import com.tondeverton.demo.contactapi.validators.ContactsSearchString;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.noContent;

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
        return contactsUseCase.update(id, request).orElseThrow(ContactNotFoundException::new);
    }

    @GetMapping(value = "{id}", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    public ResponseEntity<Contact> get(@NotNull @PathVariable UUID id) {
        return contactsUseCase.get(id).map(ResponseEntity::ok).orElseGet(() -> noContent().build());
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    public GetAllContactsResponse getAll(
            @RequestParam(required = false, defaultValue = "") @ContactsSearchString String search,
            @RequestParam(required = false, defaultValue = "0") @ContactsSearchPageSize int page
    ) {
        var contactsPage = contactsUseCase.getAll(search, page);

        return new GetAllContactsResponse(
                contactsPage.getNumber(),
                contactsPage.getTotalPages(),
                contactsPage.get().toList()
        );
    }

    @DeleteMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@NotNull @PathVariable UUID id) {
        var wasDeleted = contactsUseCase.delete(id).orElseThrow(ContactNotFoundException::new);

        if (wasDeleted)
            return;

        throw new RuntimeException();
    }
}
