package com.tondeverton.demo.contactapi.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
interface CrudContactRepository extends CrudRepository<ContactDataSource, Long> {

    Optional<ContactDataSource> findById(UUID id);
}
