package com.academy.fintech.origination.public_interface.agreement.exception;

public class NotFoundException extends RuntimeException{
    public NotFoundException(String message) {
        super(message);
    }
}
