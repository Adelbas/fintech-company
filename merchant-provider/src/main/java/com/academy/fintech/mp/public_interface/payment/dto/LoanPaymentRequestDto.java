package com.academy.fintech.mp.public_interface.payment.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record LoanPaymentRequestDto(

        String clientEmail,
        UUID agreementNumber,
        BigDecimal amount
) {
}
