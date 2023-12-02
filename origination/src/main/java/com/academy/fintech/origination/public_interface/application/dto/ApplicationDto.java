package com.academy.fintech.origination.public_interface.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ApplicationDto(
        @NotBlank(message = "Empty first name")
        String firstName,
        @NotBlank(message = "Empty last name")
        String lastName,
        @Email(message = "Invalid email")
        String email,
        @Positive(message = "Invalid salary")
        BigDecimal salary,
        @Positive(message = "Invalid requested disbursement amount")
        BigDecimal requestedDisbursementAmount
) {
}