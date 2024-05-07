package com.tondeverton.demo.contactapi.repositories;

import com.tondeverton.demo.contactapi.providers.VariableProvider;
import com.tondeverton.demo.contactapi.providers.Variables;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class DataSourceContactRepository implements ContactRepository {

    private final CrudContactRepository repository;
    private final VariableProvider variableProvider;

    public DataSourceContactRepository(CrudContactRepository repository, VariableProvider variableProvider) {
        this.repository = repository;
        this.variableProvider = variableProvider;
    }

    @Override
    public Contact add(ContactToSave toSave) {
        var contact = new ContactDataSource();
        contact.setFirstName(toSave.getFirstName());
        contact.setLastName(toSave.getLastName());
        contact.setDisplayName(toSave.getDisplayName());
        contact.setPhoneNumber(toSave.getPhoneNumber());
        contact.setEmail(toSave.getEmail());

        contact.setIdentifier(UUID.randomUUID());

        return repository.save(contact).clone();
    }

    @Override
    public Optional<Contact> getByIdentifier(UUID id) {
        return repository.findByIdentifier(id).map(c -> c);
    }

    @Override
    public Page<Contact> getAll() {
        var maxPageSize = variableProvider.getValueAsInt(Variables.CONTACTS_SEARCH_MAX_PAGE_SIZE);
        return this.getAll(0, maxPageSize, "", 0);
    }

    @Override
    public Page<Contact> getAll(String search, double minPercentSimilarity) {
        var maxPageSize = variableProvider.getValueAsInt(Variables.CONTACTS_SEARCH_MAX_PAGE_SIZE);
        return this.getAll(0, maxPageSize, search, minPercentSimilarity);
    }

    @Override
    public Page<Contact> getAll(int page, int pageSize) {
        return this.getAll(page, pageSize, "", 0);
    }

    @Override
    public Page<Contact> getAll(int page, int pageSize, String search, double minPercentSimilarity) {
        // TODO: calc maxDistance
        return repository.findByLevenshteinSimilarity(search, 30, PageRequest.of(page, pageSize))
                .map(ContactEntity::clone)
                .map(c -> c);
    }

    @Override
    public Optional<Contact> update(UUID id, ContactToSave contact) {
        return Optional.empty();
    }

    @Override
    public boolean deleteById(UUID id) {
        return false;
    }
}
