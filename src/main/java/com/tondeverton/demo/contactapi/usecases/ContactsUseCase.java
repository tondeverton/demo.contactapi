package com.tondeverton.demo.contactapi.usecases;

import com.tondeverton.demo.contactapi.providers.VariableProvider;
import com.tondeverton.demo.contactapi.repositories.Contact;
import com.tondeverton.demo.contactapi.repositories.ContactRepository;
import com.tondeverton.demo.contactapi.repositories.ContactToSave;
import com.tondeverton.demo.contactapi.validators.ContactsSearchPageSize;
import com.tondeverton.demo.contactapi.validators.ContactsSearchString;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;
import java.util.UUID;

import static com.tondeverton.demo.contactapi.providers.Variables.CONTACTS_SEARCH_MAX_PAGE_SIZE;
import static com.tondeverton.demo.contactapi.providers.Variables.CONTACTS_SEARCH_MIN_PERCENT_SIMILARITY;
import static org.apache.logging.log4j.util.Strings.isBlank;

@Service
@Validated
public class ContactsUseCase {

    private final ContactRepository contactRepository;
    private final VariableProvider variableProvider;

    public ContactsUseCase(ContactRepository contactRepository, VariableProvider variableProvider) {
        this.contactRepository = contactRepository;
        this.variableProvider = variableProvider;
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

    public Page<Contact> getAll(@ContactsSearchString String search, @ContactsSearchPageSize int page) {
        if (isBlank(search) && page <= 0) {
            return contactRepository.getAll();
        }

        var maxPageSize = variableProvider.getValueAsInt(CONTACTS_SEARCH_MAX_PAGE_SIZE);
        if (isBlank(search)) {
            return contactRepository.getAll(page, maxPageSize);
        }

        var minPercentSimilarity = variableProvider.getValueAsInt(CONTACTS_SEARCH_MIN_PERCENT_SIMILARITY);
        if (page <= 0) {
            return contactRepository.getAll(search, minPercentSimilarity);
        }

        return contactRepository.getAll(page, maxPageSize, search, minPercentSimilarity);
    }

    public Optional<Boolean> delete(@NotNull UUID id) {
        return Optional.of(contactRepository.deleteById(id));
    }
}
