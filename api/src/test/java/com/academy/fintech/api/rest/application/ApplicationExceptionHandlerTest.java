package com.academy.fintech.api.rest.application;

import com.academy.fintech.api.rest.dto.ErrorMessage;
import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class ApplicationExceptionHandlerTest {

    private ApplicationExceptionHandler applicationExceptionHandler;

    @BeforeEach
    void setUp() {
        applicationExceptionHandler = new ApplicationExceptionHandler();
    }

    @Test
    void handleStatusRuntimeExceptionNotFound() {
        StatusRuntimeException statusRuntimeException = new StatusRuntimeException(Status.NOT_FOUND);

        ResponseEntity<ErrorMessage> response = applicationExceptionHandler.handleStatusRuntimeException(statusRuntimeException);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().details()).isNull();
        assertThat(response.getBody().status()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void handleStatusRuntimeExceptionInvalidArguments() {
        Metadata metadata = new Metadata();
        metadata.put(Metadata.Key.of("email",Metadata.ASCII_STRING_MARSHALLER), "Invalid email");
        StatusRuntimeException statusRuntimeException = new StatusRuntimeException(Status.INVALID_ARGUMENT, metadata);

        ResponseEntity<ErrorMessage> response = applicationExceptionHandler.handleStatusRuntimeException(statusRuntimeException);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getBody().details())
                .isNotNull()
                .hasFieldOrPropertyWithValue("email","Invalid email");
    }
}