package com.tondeverton.demo.contactapi.repositories;

import java.util.UUID;

public interface Contact {
    UUID id();
    String firstName();
    String lastName();
    String displayName();
    String phoneNumber();
    String email();
}
