package com.tondeverton.demo.contactapi.repositories;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "contacts")
public class ContactDataSource extends ContactEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long identifier;

    public Long getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Long identifier) {
        this.identifier = identifier;
    }
}
