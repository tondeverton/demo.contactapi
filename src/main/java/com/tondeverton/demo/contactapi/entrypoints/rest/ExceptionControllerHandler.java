package com.tondeverton.demo.contactapi.entrypoints.rest;

import com.tondeverton.demo.contactapi.exceptions.PreconditionException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.apache.logging.log4j.util.Strings.isNotBlank;
import static org.apache.tomcat.util.http.fileupload.FileUploadBase.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import static org.springframework.http.HttpStatus.PRECONDITION_FAILED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.internalServerError;

@ControllerAdvice
public class ExceptionControllerHandler {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<MessageResponse> exception(Exception exception) {
        if (
                exception instanceof MethodArgumentNotValidException
                        || exception instanceof ConstraintViolationException
        ) {
            return badRequest();
        }

        if (exception instanceof HttpRequestMethodNotSupportedException) {
            return ResponseEntity.status(METHOD_NOT_ALLOWED).build();
        }

        if (exception instanceof PreconditionException) {
            return preConditionFailed(exception);
        }

        return internalServerError()
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .body(new MessageResponse("Internal error"));
    }

    private static ResponseEntity<MessageResponse> preConditionFailed(Exception exception) {
        var body = isNotBlank(exception.getMessage())
                ? new MessageResponse(exception.getMessage())
                : null;
        return ResponseEntity.status(PRECONDITION_FAILED).body(body);
    }

    private static ResponseEntity<MessageResponse> badRequest() {
        return ResponseEntity.badRequest()
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .body(new MessageResponse("Invalid request"));
    }
}
