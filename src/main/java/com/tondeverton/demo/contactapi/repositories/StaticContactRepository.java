package com.tondeverton.demo.contactapi.repositories;

import com.tondeverton.demo.contactapi.utilities.PageConverter;
import com.tondeverton.demo.contactapi.utilities.PageConverterUtil;
import com.tondeverton.demo.contactapi.utilities.StringSimilarityLevenshtein;
import com.tondeverton.demo.contactapi.utilities.StringSimilarityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

import static java.lang.String.format;
import static java.util.UUID.randomUUID;
import static org.apache.logging.log4j.util.Strings.isNotBlank;

@Repository
public class StaticContactRepository implements ContactRepository {

    protected static final Collection<ContactEntity> contacts = new ArrayList<>();

    private final StringSimilarityUtil stringSimilarity;
    private final PageConverterUtil pageConverter;

    public StaticContactRepository(
            @Autowired(required = false) StringSimilarityUtil stringSimilarity,
            @Autowired(required = false) PageConverterUtil pageConverter
    ) {
        this.stringSimilarity = stringSimilarity == null ? new StringSimilarityLevenshtein() : stringSimilarity;
        this.pageConverter = pageConverter == null ? new PageConverter() : pageConverter;
    }

    @Override
    public Contact add(ContactToSave toSave) {
        var contact = new ContactEntity();
        contact.setFirstName(toSave.getFirstName());
        contact.setLastName(toSave.getLastName());
        contact.setDisplayName(toSave.getDisplayName());
        contact.setPhoneNumber(toSave.getPhoneNumber());
        contact.setEmail(toSave.getEmail());

        contact.setId(randomUUID());

        contacts.add(contact);

        return contact.clone();
    }

    @Override
    public Optional<Contact> getById(UUID id) {
        return contacts.stream().filter(c -> id.equals(c.getId())).findFirst().map(c -> c);
    }

    @Override
    public Page<Contact> getAll() {
        return this.getAll(0, 30, "", 0);
    }

    @Override
    public Page<Contact> getAll(String search, double minPercentSimilarity) {
        return this.getAll(0, 30, search, minPercentSimilarity);
    }

    @Override
    public Page<Contact> getAll(int page, int pageSize) {
        return this.getAll(page, pageSize, "", 0);
    }

    @Override
    public Page<Contact> getAll(int page, int pageSize, String search, double minPercentSimilarity) {
        Predicate<ContactEntity> filterBySearch = isNotBlank(search)
                ? c -> {
            var contactProperties = format(
                    "%s %s %s %s %s",
                    c.getFirstName(), c.getLastName(), c.getDisplayName(), c.getPhoneNumber(), c.getEmail()
            );

            return stringSimilarity.percentageBetween(search, contactProperties) >= minPercentSimilarity;
        }
                : c -> true;

        var contacts = StaticContactRepository.contacts.stream().filter(filterBySearch)
                .map(ContactEntity::clone)
                .map(c -> (Contact) c)
                .toList();
        return pageConverter.collectionToPage(contacts, page, pageSize);
    }

    @Override
    public Optional<Contact> update(UUID id, ContactToSave toSave) {
        var contact = contacts.stream().filter(c -> c.getId().equals(id)).findFirst();
        contact.ifPresent(c -> {
            c.setFirstName(toSave.getFirstName());
            c.setLastName(toSave.getLastName());
            c.setDisplayName(toSave.getDisplayName());
            c.setPhoneNumber(toSave.getPhoneNumber());
            c.setEmail(toSave.getEmail());
        });
        return contact.map(ContactEntity::clone);
    }

    @Override
    public boolean deleteById(UUID id) {
        return contacts.removeIf(c -> id.equals(c.getId()));
    }
}
