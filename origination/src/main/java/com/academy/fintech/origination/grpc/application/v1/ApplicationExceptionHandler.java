package com.academy.fintech.origination.grpc.application.v1;

import com.academy.fintech.origination.public_interface.application.exception.ApplicationDuplicateException;
import com.academy.fintech.origination.public_interface.application.exception.NotFoundException;
import io.grpc.Metadata;
import io.grpc.Status;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.recovery.GRpcExceptionHandler;
import org.lognet.springboot.grpc.recovery.GRpcExceptionScope;
import org.lognet.springboot.grpc.recovery.GRpcServiceAdvice;

/**
 * Represents exception handler that returns {@link Status} object with a specific status number and description.
 */
@Slf4j
@GRpcServiceAdvice
public class ApplicationExceptionHandler {

    @GRpcExceptionHandler
    public Status handleNotFoundException(NotFoundException exc, GRpcExceptionScope scope) {
        log.error("NotFoundException caught: ", exc);
        return Status.NOT_FOUND.withDescription(exc.getMessage()).withCause(exc);
    }

    /**
     * Handles {@link ApplicationDuplicateException}.
     * Put applicationId in Metadata, that will be available in grpc trailers.
     *
     * @param exc ApplicationDuplicateException
     * @param scope GRpcExceptionScope
     * @return Status
     */
    @GRpcExceptionHandler
    public Status handleApplicationDuplicateException(ApplicationDuplicateException exc, GRpcExceptionScope scope) {
        log.error("ApplicationDuplicateException caught: ", exc);
        scope.getResponseHeaders().put(Metadata.Key.of("application_id", Metadata.ASCII_STRING_MARSHALLER), exc.getMessage());
        return Status.ALREADY_EXISTS.withDescription("Application is already created");
    }

    /**
     * Handles {@link ConstraintViolationException}.
     * Put error messages for each field in Metadata, that will be available in grpc trailers.
     *
     * @param exc ConstraintViolationException
     * @param scope GRpcExceptionScope
     * @return Status
     */
    @GRpcExceptionHandler
    public Status handleConstraintViolationException(ConstraintViolationException exc, GRpcExceptionScope scope) {
        log.error("ConstraintViolationException caught: ", exc);

        for (ConstraintViolation<?> violation : exc.getConstraintViolations()) {
            String propertyPath = violation.getPropertyPath().toString();
            String fieldName = propertyPath.substring(propertyPath.lastIndexOf('.') + 1);
            scope.getResponseHeaders().put(
                    Metadata.Key.of(fieldName, Metadata.ASCII_STRING_MARSHALLER),
                    violation.getMessage()
            );
        }

        return Status.INVALID_ARGUMENT.withDescription("Validation failed").withCause(exc);
    }

    @GRpcExceptionHandler
    public Status handleUnhandledExceptions(Exception exc, GRpcExceptionScope scope) {
        log.error("Exception caught: ", exc);
        return Status.INTERNAL.withDescription(exc.getMessage()).withCause(exc);
    }
}
