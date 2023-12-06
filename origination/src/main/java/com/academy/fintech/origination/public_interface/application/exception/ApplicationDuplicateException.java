package com.academy.fintech.origination.public_interface.application.exception;

import java.util.UUID;

public class ApplicationDuplicateException extends RuntimeException {
    public ApplicationDuplicateException(UUID applicationId) {
        super(applicationId.toString());
    }
}
