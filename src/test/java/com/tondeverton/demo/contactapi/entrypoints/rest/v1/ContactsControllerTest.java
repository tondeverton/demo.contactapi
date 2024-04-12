package com.tondeverton.demo.contactapi.entrypoints.rest.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tondeverton.demo.contactapi.testutilities.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.LinkedHashMap;
import java.util.List;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.RequestEntity.post;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ContactsControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper mapper;

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

        var post = post(format("http://localhost:%s/v1/contacts", port))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .body(request);
        var response = this.restTemplate.exchange(post, Object.class);

        assertThat(response.getStatusCode().value()).isEqualTo(CREATED.value());
        assertThat(response.getHeaders()).contains(entry("Content-Type", List.of(APPLICATION_JSON_VALUE)));
    }


    @Test
    void POSTContacts_givenAnyInvalidContact_shouldReturnsStatusCode400() {
        var firstName = Faker.firstName();
        var lastName = Faker.lastName();
        var displayName = Faker.nickname();
        var wrongPhoneNumber = Faker.phoneNumber().contains(Faker.word());
        var email = Faker.email();

        var request = format("""
                {
                "first_name": "%s",
                "last_name": "%s",
                "display_name": "%s",
                "phone_number": "%s",
                "email": "%s"
                }""", firstName, lastName, displayName, wrongPhoneNumber, email);

        var post = post(format("http://localhost:%s/v1/contacts", port))
                .contentType(APPLICATION_JSON)
                .body(request);
        var response = this.restTemplate.exchange(post, LinkedHashMap.class);

        assertThat(response.getStatusCode().value()).isEqualTo(BAD_REQUEST.value());
    }
}
