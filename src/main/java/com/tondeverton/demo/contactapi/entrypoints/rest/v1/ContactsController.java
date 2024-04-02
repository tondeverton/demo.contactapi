package com.tondeverton.demo.contactapi.entrypoints.rest.v1;

import com.tondeverton.demo.contactapi.entrypoints.rest.v1.reqsress.SaveContactRequest;
import com.tondeverton.demo.contactapi.repositories.Contact;
import com.tondeverton.demo.contactapi.usecases.ContactsUseCase;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/v1/contacts")
@Validated
public class ContactsController {

    private final ContactsUseCase contactsUseCase;

    public ContactsController(ContactsUseCase contactsUseCase) {
        this.contactsUseCase = contactsUseCase;
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public Contact save(@Valid @NotNull @RequestBody SaveContactRequest request) {
        return contactsUseCase.save(request);
    }
}
