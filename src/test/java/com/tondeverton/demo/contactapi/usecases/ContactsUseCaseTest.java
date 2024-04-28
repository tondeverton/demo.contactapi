package com.tondeverton.demo.contactapi.usecases;

import com.tondeverton.demo.contactapi.repositories.ContactRepository;
import com.tondeverton.demo.contactapi.testutilities.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class ContactsUseCaseTest {

    @MockBean
    private ContactRepository repository;
    @Autowired
    private ContactsUseCase useCase;

    @Test
    void getAll_nullSearchAndNegativePage_shouldCallExpectedRepositoryMethod() {
        useCase.getAll(null, -1);

        verify(repository).getAll();
    }

    @Test
    void getAll_AnySearchAndNegativePage_shouldCallExpectedRepositoryMethod() {
        useCase.getAll(Faker.word(), -1);

        verify(repository).getAll(anyString(), anyDouble());
    }

    @Test
    void getAll_NullSearchAndPositivePage_shouldCallExpectedRepositoryMethod() {
        useCase.getAll(null, 5);

        verify(repository).getAll(anyInt(), anyInt());
    }

    @Test
    void getAll_ANySearchAndPositivePage_shouldCallExpectedRepositoryMethod() {
        useCase.getAll(Faker.word(), 5);

        verify(repository).getAll(anyInt(), anyInt(), anyString(), anyDouble());
    }
}
