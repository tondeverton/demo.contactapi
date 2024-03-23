package com.tondeverton.demo.contactapi.repositories;

import com.tondeverton.demo.contactapi.utilities.StringSimilarityUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class StaticContactRepositoryTest {

    private final StringSimilarityUtil stringSimilarity;
    private final StaticContactRepository repository;

    public StaticContactRepositoryTest() {
        stringSimilarity = mock(StringSimilarityUtil.class);
        this.repository = new StaticContactRepository(stringSimilarity);
    }

    @BeforeEach
    public void resetMocks() {
        reset(stringSimilarity);
    }

    @Test
    void add_givenSomeContactToInsert_shouldReturnsContactWithTheSamePropertiesFromInput() {
        var toInsert = mock(ContactToInsert.class);

        when(toInsert.getFirstName()).thenReturn("a");
        when(toInsert.getLastName()).thenReturn("b");
        when(toInsert.getDisplayName()).thenReturn("c");
        when(toInsert.getPhoneNumber()).thenReturn("1");
        when(toInsert.getEmail()).thenReturn("@");

        var persisted = repository.add(toInsert);

        assertEquals("a", persisted.getFirstName());
        assertEquals("b", persisted.getLastName());
        assertEquals("c", persisted.getDisplayName());
        assertEquals("1", persisted.getPhoneNumber());
        assertEquals("@", persisted.getEmail());
    }

    @Test
    void add_givenAnyContactToInsert_shouldReturnsContactWithNotBlankId() {
        var toInsert = mock(ContactToInsert.class);

        var persisted = repository.add(toInsert);

        assertNotNull(persisted.getId());
        assertNotEquals(0L, persisted.getId().getMostSignificantBits());
        assertNotEquals(0L, persisted.getId().getLeastSignificantBits());
    }
}
