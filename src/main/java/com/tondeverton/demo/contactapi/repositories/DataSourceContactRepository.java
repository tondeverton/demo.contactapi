package com.tondeverton.demo.contactapi.repositories;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class DataSourceContactRepository implements ContactRepository {

    private final CrudContactRepository repository;

    public DataSourceContactRepository(CrudContactRepository repository) {
        this.repository = repository;
    }

    @Override
    public Contact add(ContactToSave toSave) {
        var contact = new ContactDataSource();
        contact.setFirstName(toSave.getFirstName());
        contact.setLastName(toSave.getLastName());
        contact.setDisplayName(toSave.getDisplayName());
        contact.setPhoneNumber(toSave.getPhoneNumber());
        contact.setEmail(toSave.getEmail());

        contact.setId(UUID.randomUUID());

        return repository.save(contact).clone();
    }

    @Override
    public Optional<Contact> getById(UUID id) {
        return repository.findById(id).map(c -> c);
    }

    @Override
    public Page<Contact> getAll() {
        return null;
    }

    @Override
    public Page<Contact> getAll(String search, double minPercentSimilarity) {
        return null;
    }

    @Override
    public Page<Contact> getAll(int page, int pageSize) {
        return null;
    }

    @Override
    public Page<Contact> getAll(int page, int pageSize, String search, double minPercentSimilarity) {
        return null;
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
