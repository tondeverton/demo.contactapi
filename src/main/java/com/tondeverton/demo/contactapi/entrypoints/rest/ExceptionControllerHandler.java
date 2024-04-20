package com.tondeverton.demo.contactapi.entrypoints.rest;

import com.tondeverton.demo.contactapi.exceptions.PreconditionException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.apache.logging.log4j.util.Strings.isNotBlank;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@ControllerAdvice
public class ExceptionControllerHandler {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<MessageResponse> exception(Exception exception) {
        if (
                exception instanceof MethodArgumentNotValidException
                || exception instanceof ConstraintViolationException
        ) {
            return ResponseEntity.status(BAD_REQUEST)
                    .header("Content-Type", APPLICATION_JSON_VALUE)
                    .body(new MessageResponse("Invalid request"));
        }

        if (exception instanceof HttpRequestMethodNotSupportedException) {
            return ResponseEntity.status(METHOD_NOT_ALLOWED).build();
        }

        if (exception instanceof PreconditionException) {
            var body = isNotBlank(exception.getMessage())
                    ? new MessageResponse(exception.getMessage())
                    : null;
            return ResponseEntity.status(PRECONDITION_FAILED)
                    .body(body);
        }

        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .header("Content-Type", APPLICATION_JSON_VALUE)
                .body(new MessageResponse("Internal error"));
    }
}
