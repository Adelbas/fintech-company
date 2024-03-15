package com.academy.fintech.pg.public_interface.payment.exeption;

public class NotFoundException extends RuntimeException{
    public NotFoundException(String message) {
        super(message);
    }
}