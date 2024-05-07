package com.tondeverton.demo.contactapi.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
interface CrudContactRepository extends CrudRepository<ContactDataSource, Long>, PagingAndSortingRepository<ContactDataSource, Long> {

    Optional<ContactDataSource> findById(UUID id);

    @Query(value = """
            SELECT *, COUNT(*) OVER() AS total_count FROM contacts
            WHERE levenshtein(:search, concat(firstName, lastName, displayName, phoneNumber, email)) <= :maxDistance
            ORDER BY levenshtein(:searchTerm, username)
            OFFSET :offset LIMIT :limit
            """,
            nativeQuery = true
    )
    Page<ContactDataSource> findByLevenshteinSimilarity(String search, int maxDistance, Pageable pageable);
}
