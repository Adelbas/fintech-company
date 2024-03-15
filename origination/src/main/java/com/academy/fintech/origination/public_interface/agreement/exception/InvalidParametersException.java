package com.academy.fintech.origination.public_interface.agreement.exception;

public class InvalidParametersException extends RuntimeException{
    public InvalidParametersException(String message) {
        super(message);
    }
}
