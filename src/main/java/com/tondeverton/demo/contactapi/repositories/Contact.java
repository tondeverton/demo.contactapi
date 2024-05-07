package com.tondeverton.demo.contactapi.repositories;

import java.util.UUID;

public interface Contact {
    UUID getIdentifier();

    String getFirstName();

    String getLastName();

    String getDisplayName();

    String getPhoneNumber();

    String getEmail();
}
