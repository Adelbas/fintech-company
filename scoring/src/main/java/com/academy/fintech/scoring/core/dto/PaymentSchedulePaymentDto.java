package com.academy.fintech.scoring.core.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record PaymentSchedulePaymentDto(
        int period,
        BigDecimal payment,
        BigDecimal interest,
        BigDecimal principal,
        LocalDateTime paymentDate,
        PaymentStatus status
) { }
