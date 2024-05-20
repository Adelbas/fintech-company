package com.academy.fintech.pe.public_interface.agreement.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record LoanPaymentRequestDto(
        UUID agreementNumber,
        BigDecimal amount
) {
}
