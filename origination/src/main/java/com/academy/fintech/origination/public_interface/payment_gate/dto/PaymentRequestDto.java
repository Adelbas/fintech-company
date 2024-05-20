package com.academy.fintech.origination.public_interface.payment_gate.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record PaymentRequestDto(
        String clientEmail,
        UUID agreementNumber,
        BigDecimal amount
) { }
