package com.tondeverton.demo.contactapi.entrypoints.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@ControllerAdvice
public class ExceptionControllerHandler {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<MessageResponse> exception(Exception exception) {
        if (exception instanceof MethodArgumentNotValidException) {
            return ResponseEntity.status(BAD_REQUEST)
                    .header("Content-Type", APPLICATION_JSON_VALUE)
                    .body(new MessageResponse("Invalid request"));
        }

        if (exception instanceof HttpRequestMethodNotSupportedException) {
            return ResponseEntity.status(METHOD_NOT_ALLOWED).build();
        }

        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .header("Content-Type", APPLICATION_JSON_VALUE)
                .body(new MessageResponse("Internal error"));
    }
}
