package com.academy.fintech.pe.grpc.agreement.v1;

import com.academy.fintech.pe.public_interface.agreement.exception.InvalidParametersException;
import com.academy.fintech.pe.public_interface.agreement.exception.NotFoundException;
import io.grpc.Status;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.recovery.GRpcExceptionHandler;
import org.lognet.springboot.grpc.recovery.GRpcExceptionScope;
import org.lognet.springboot.grpc.recovery.GRpcServiceAdvice;

/**
 * Represents exception handler that returns {@link Status} object with a specific status number and description.
 */
@Slf4j
@GRpcServiceAdvice
public class AgreementExceptionHandler {

    @GRpcExceptionHandler
    public Status handleNotFoundException(NotFoundException exc, GRpcExceptionScope scope) {
        log.error("NotFoundException caught: ", exc);
        return Status.NOT_FOUND.withDescription(exc.getMessage()).withCause(exc);
    }

    @GRpcExceptionHandler
    public Status handleInvalidParametersException(InvalidParametersException exc, GRpcExceptionScope scope) {
        log.error("InvalidParametersException caught: ", exc);
        return Status.INVALID_ARGUMENT.withDescription(exc.getMessage()).withCause(exc);
    }

    @GRpcExceptionHandler
    public Status handleUnhandledExceptions(Exception exc, GRpcExceptionScope scope) {
        log.error("Exception caught: ", exc);
        return Status.INTERNAL.withDescription(exc.getMessage()).withCause(exc);
    }
}
