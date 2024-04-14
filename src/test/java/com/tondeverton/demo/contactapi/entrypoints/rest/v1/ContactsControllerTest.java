package com.tondeverton.demo.contactapi.entrypoints.rest.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.tondeverton.demo.contactapi.testutilities.Faker;
import com.tondeverton.demo.contactapi.testutilities.FakerFactory;
import com.tondeverton.demo.contactapi.usecases.ContactsUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.RequestEntity.*;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class ContactsControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ContactsUseCase contactsUseCase;

    private final String uriTemplate;

    public ContactsControllerTest(@LocalServerPort int port) {
        uriTemplate = format("http://localhost:%s/v1/contacts", port);
    }

    @Test
    void POSTContacts_givenAnyValidContact_shouldReturnsStatusCode201WithJSONBody() {
        var firstName = Faker.firstName();
        var lastName = Faker.lastName();
        var displayName = Faker.nickname();
        var phoneNumber = Faker.phoneNumber();
        var email = Faker.email();

        var request = format("""
                {
                "first_name": "%s",
                "last_name": "%s",
                "display_name": "%s",
                "phone_number": "%s",
                "email": "%s"
                }""", firstName, lastName, displayName, phoneNumber, email);

        when(contactsUseCase.save(any())).thenReturn(FakerFactory.contactEntity());

        var post = post(uriTemplate)
                .contentType(APPLICATION_JSON)
                .body(request);
        var response = this.restTemplate.exchange(post, Object.class);

        assertThat(response.getStatusCode()).isEqualTo(CREATED);
        assertThat(response.getHeaders()).contains(entry("Content-Type", List.of(APPLICATION_JSON_VALUE)));
    }

    @Test
    void POSTContacts_givenAnyInvalidContactData_shouldReturnsStatusCode400WithSpecificErrorMessage() {
        var firstName = Faker.firstName();
        var lastName = Faker.lastName();
        var displayName = Faker.nickname();
        var wrongPhoneNumber = "My number: ".concat(Faker.phoneNumber());
        var email = Faker.email();

        var request = format("""
                {
                "first_name": "%s",
                "last_name": "%s",
                "display_name": "%s",
                "phone_number": "%s",
                "email": "%s"
                }""", firstName, lastName, displayName, wrongPhoneNumber, email);

        var post = post(uriTemplate)
                .contentType(APPLICATION_JSON)
                .body(request);
        var response = this.restTemplate.exchange(post, String.class);
        var message = JsonPath.read(response.getBody(), "$.message");

        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(message).isEqualTo("Invalid request");
    }

    @Test
    void POSTContacts_givenAnyContactWithoutRequiredData_shouldReturnsStatusCode400WithSpecificErrorMessage() {
        var firstName = Faker.firstName();
        var lastName = Faker.lastName();
        var phoneNumber = Faker.phoneNumber();
        var email = Faker.email();

        var request = format("""
                {
                "first_name": "%s",
                "last_name": "%s",
                "phone_number": "%s",
                "email": "%s"
                }""", firstName, lastName, phoneNumber, email);

        var post = post(uriTemplate)
                .contentType(APPLICATION_JSON)
                .body(request);
        var response = this.restTemplate.exchange(post, String.class);
        var message = JsonPath.read(response.getBody(), "$.message");

        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(message).isEqualTo("Invalid request");
    }

    @Test
    void PUTContacts_givenExistentContactIdAndAnyValidContact_shouldReturnsStatusCode200WithJSONBody() {
        var firstName = Faker.firstName();
        var lastName = Faker.lastName();
        var displayName = Faker.nickname();
        var phoneNumber = Faker.phoneNumber();
        var email = Faker.email();

        var request = format("""
                {
                "first_name": "%s",
                "last_name": "%s",
                "display_name": "%s",
                "phone_number": "%s",
                "email": "%s"
                }""", firstName, lastName, displayName, phoneNumber, email);

        var contactId = UUID.randomUUID();

        when(contactsUseCase.update(any(), any())).thenReturn(Optional.of(FakerFactory.contactEntity()));

        var post = put(uriTemplate.concat("/").concat(contactId.toString()))
                .contentType(APPLICATION_JSON)
                .body(request);
        var response = this.restTemplate.exchange(post, Object.class);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getHeaders()).contains(entry("Content-Type", List.of(APPLICATION_JSON_VALUE)));
    }

    @Test
    void PUTContacts_givenExistentContactIdAndAnyInvalidContact_shouldReturnsStatusCode400WithSpecificErrorMessage() {
        var firstName = Faker.firstName();
        var lastName = Faker.lastName();
        var displayName = Faker.nickname();
        var wrongPhoneNumber = "My number:".concat(Faker.phoneNumber());
        var email = Faker.email();

        var request = format("""
                {
                "first_name": "%s",
                "last_name": "%s",
                "display_name": "%s",
                "phone_number": "%s",
                "email": "%s"
                }""", firstName, lastName, displayName, wrongPhoneNumber, email);

        var contactId = UUID.randomUUID();

        when(contactsUseCase.update(any(), any())).thenReturn(Optional.of(FakerFactory.contactEntity()));

        var post = put(uriTemplate.concat("/").concat(contactId.toString()))
                .contentType(APPLICATION_JSON)
                .body(request);
        var response = this.restTemplate.exchange(post, Object.class);
        var message = JsonPath.read(response.getBody(), "$.message");

        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(message).isEqualTo("Invalid request");
    }

    @Test
    void PUTContacts_givenExistentContactIdAndAnyContactWithoutRequiredData_shouldReturnsStatusCode400WithSpecificErrorMessage() {
        var firstName = Faker.firstName();
        var lastName = Faker.lastName();
        var phoneNumber = Faker.phoneNumber();
        var email = Faker.email();

        var request = format("""
                {
                "first_name": "%s",
                "last_name": "%s",
                "phone_number": "%s",
                "email": "%s"
                }""", firstName, lastName, phoneNumber, email);

        var contactId = UUID.randomUUID();

        when(contactsUseCase.update(any(), any())).thenReturn(Optional.of(FakerFactory.contactEntity()));

        var post = put(uriTemplate.concat("/").concat(contactId.toString()))
                .contentType(APPLICATION_JSON)
                .body(request);
        var response = this.restTemplate.exchange(post, Object.class);
        var message = JsonPath.read(response.getBody(), "$.message");

        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(message).isEqualTo("Invalid request");
    }

    @Test
    void PUTContacts_givenNonexistentContactIdAndAnyValidContact_shouldReturnsStatusCode412WithSpecificErrorMessage() {
        var firstName = Faker.firstName();
        var lastName = Faker.lastName();
        var displayName = Faker.nickname();
        var phoneNumber = Faker.phoneNumber();
        var email = Faker.email();

        var request = format("""
                {
                "first_name": "%s",
                "last_name": "%s",
                "display_name": "%s",
                "phone_number": "%s",
                "email": "%s"
                }""", firstName, lastName, displayName, phoneNumber, email);

        var contactId = UUID.randomUUID();

        when(contactsUseCase.update(any(), any())).thenReturn(Optional.empty());

        var post = put(uriTemplate.concat("/").concat(contactId.toString()))
                .contentType(APPLICATION_JSON)
                .body(request);
        var response = this.restTemplate.exchange(post, Object.class);
        var message = JsonPath.read(response.getBody(), "$.message");

        assertThat(response.getStatusCode()).isEqualTo(PRECONDITION_FAILED);
        assertThat(message).isEqualTo("Contact not found");
    }

    @Test
    void GETContacts_givenAnyExistentId_shouldReturnsStatusCode200AndJSONBody() {
        var existentContactId = UUID.randomUUID().toString();
        when(contactsUseCase.get(any())).thenReturn(Optional.of(FakerFactory.contactEntity()));

        var response = restTemplate.exchange(get(uriTemplate.concat("/").concat(existentContactId)).build(), Object.class);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getHeaders()).contains(entry("Content-Type", List.of(APPLICATION_JSON_VALUE)));
    }

    @Test
    void GETContacts_givenANonexistentId_shouldReturnsStatusCode204() {
        var existentContactId = UUID.randomUUID().toString();
        when(contactsUseCase.get(any())).thenReturn(Optional.empty());

        var response = restTemplate.exchange(get(uriTemplate.concat("/").concat(existentContactId)).build(), Object.class);

        assertThat(response.getStatusCode()).isEqualTo(NO_CONTENT);
    }
}
