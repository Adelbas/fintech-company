package com.academy.fintech.origination.grpc.application.v1;

import com.academy.fintech.origination.public_interface.application.exception.ApplicationDuplicateException;
import com.academy.fintech.origination.public_interface.application.exception.NotFoundException;
import io.grpc.Metadata;
import io.grpc.Status;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lognet.springboot.grpc.recovery.GRpcExceptionScope;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ApplicationExceptionHandlerTest {

    private ApplicationExceptionHandler applicationExceptionHandler;

    @BeforeEach
    void setUp() {
        applicationExceptionHandler = new ApplicationExceptionHandler();
    }

    @Test
    void handleNotFoundException() {
        Status.Code expectedCode = Status.Code.NOT_FOUND;
        Status status = applicationExceptionHandler.handleNotFoundException(mock(NotFoundException.class), mock(GRpcExceptionScope.class));
        assertThat(status.getCode()).isEqualTo(expectedCode);
    }

    @Test
    void handleApplicationDuplicateException() {
        UUID applicationId = UUID.randomUUID();
        ApplicationDuplicateException applicationDuplicateException = new ApplicationDuplicateException(applicationId);
        GRpcExceptionScope gRpcExceptionScope = GRpcExceptionScope.builder().build();
        Status.Code expectedCode = Status.Code.ALREADY_EXISTS;

        Status status = applicationExceptionHandler.handleApplicationDuplicateException(applicationDuplicateException, gRpcExceptionScope);
        assertThat(status.getCode()).isEqualTo(expectedCode);
        assertThat(gRpcExceptionScope.getResponseHeaders()).isNotNull();
        assertThat(gRpcExceptionScope.getResponseHeaders().get(Metadata.Key.of("application_id", Metadata.ASCII_STRING_MARSHALLER)))
                .isEqualTo(applicationId.toString());
    }

    @Test
    void handleConstraintViolationException() {
        String fieldName = "email";
        String errorMessage = "error email";
        Set<ConstraintViolation<?>> constraintViolations = new HashSet<>();
        ConstraintViolationImpl<?> constraintViolation = mock(ConstraintViolationImpl.class);
        Path path = mock(Path.class);
        when(path.toString()).thenReturn("package.class." + fieldName);
        when(constraintViolation.getPropertyPath()).thenReturn(path);
        when(constraintViolation.getMessage()).thenReturn(errorMessage);
        constraintViolations.add(constraintViolation);
        ConstraintViolationException constraintViolationException = new ConstraintViolationException(constraintViolations);
        GRpcExceptionScope gRpcExceptionScope = GRpcExceptionScope.builder().build();
        Status.Code expectedCode = Status.Code.INVALID_ARGUMENT;

        Status status = applicationExceptionHandler.handleConstraintViolationException(constraintViolationException, gRpcExceptionScope);
        assertThat(status.getCode()).isEqualTo(expectedCode);
        assertThat(gRpcExceptionScope.getResponseHeaders()).isNotNull();
        assertThat(gRpcExceptionScope.getResponseHeaders().get(Metadata.Key.of(fieldName, Metadata.ASCII_STRING_MARSHALLER)))
                .isEqualTo(errorMessage);
    }

    @Test
    void handleUnhandledExceptions() {
        Status.Code expectedCode = Status.Code.INTERNAL;
        Status status = applicationExceptionHandler.handleUnhandledExceptions(mock(RuntimeException.class), mock(GRpcExceptionScope.class));
        assertThat(status.getCode()).isEqualTo(expectedCode);
    }
}