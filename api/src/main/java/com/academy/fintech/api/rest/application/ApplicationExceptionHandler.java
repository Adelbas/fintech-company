package com.academy.fintech.api.rest.application;

import com.academy.fintech.api.rest.dto.ErrorMessage;
import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Represents exception handler that returns {@link ErrorMessage} object.
 * Contains {@link ApplicationExceptionHandler#statusRuntimeExceptionHandlers} to handle different {@link StatusRuntimeException} exceptions.
 * Contains {@link ApplicationExceptionHandler#grpcToHttpStatusMap} to map {@link Status.Code} to {@link HttpStatus}.
 */
@Slf4j
@RestControllerAdvice
public class ApplicationExceptionHandler {

    Map<Status.Code, Function<StatusRuntimeException, ErrorMessage>> statusRuntimeExceptionHandlers = new EnumMap<>(Status.Code.class);
    Map<Status.Code, HttpStatus> grpcToHttpStatusMap = new HashMap<>();

    {
        statusRuntimeExceptionHandlers.put(Status.Code.INVALID_ARGUMENT, this::handleInvalidArgumentsException);
        statusRuntimeExceptionHandlers.put(Status.Code.NOT_FOUND, this::handleNotFoundException);

        grpcToHttpStatusMap.put(Status.Code.INVALID_ARGUMENT, HttpStatus.BAD_REQUEST);
        grpcToHttpStatusMap.put(Status.Code.NOT_FOUND, HttpStatus.NOT_FOUND);
        grpcToHttpStatusMap.put(Status.Code.UNAVAILABLE, HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(StatusRuntimeException.class)
    public ResponseEntity<ErrorMessage> handleStatusRuntimeException(StatusRuntimeException e) {
        ErrorMessage errorMessage = statusRuntimeExceptionHandlers
                .getOrDefault(e.getStatus().getCode(), this::handleUnhandledException).apply(e);
        return ResponseEntity
                .status(grpcToHttpStatusMap.getOrDefault(e.getStatus().getCode(), HttpStatus.INTERNAL_SERVER_ERROR))
                .body(errorMessage);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage handleUnhandledException(Exception e) {
        return ErrorMessage.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .timestamp(new Date())
                .message(e.getMessage())
                .build();
    }

    /**
     * Handles exceptions of {@link Status.Code#INVALID_ARGUMENT} type
     * Gets exception details from grpc trailers {@link Metadata} and fill them to {@link ErrorMessage}
     *
     * @param e StatusRuntimeException
     * @return ErrorMessage
     */
    private ErrorMessage handleInvalidArgumentsException(StatusRuntimeException e) {
        Map<String, String> details = new HashMap<>();
        if (e.getTrailers() != null) {
            Metadata trailers = e.getTrailers();

            for (String field : trailers.keys()) {
                if (field.equals("content-type")) {
                    continue;
                }
                Metadata.Key<String> metadataKey = Metadata.Key.of(field, Metadata.ASCII_STRING_MARSHALLER);
                String message = trailers.get(metadataKey);

                details.put(field, message);
            }
        }
        return ErrorMessage.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(new Date())
                .message(e.getMessage())
                .details(details)
                .build();
    }

    private ErrorMessage handleNotFoundException(StatusRuntimeException e) {
        return ErrorMessage.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .timestamp(new Date())
                .message(e.getMessage())
                .build();
    }
}
