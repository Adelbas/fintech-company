package com.academy.fintech.mp.public_interface.payment.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record DisbursementRequestDto(
        String clientEmail,
        BigDecimal amount
) { }