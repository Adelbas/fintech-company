package com.academy.fintech.pe.public_interface.agreement.dto;

import com.academy.fintech.pe.core.service.agreement.db.payment_schedule.entity.enums.PaymentStatus;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record PaymentSchedulePaymentDto(
        int period,
        BigDecimal payment,
        BigDecimal interest,
        BigDecimal principal,
        BigDecimal balance,
        LocalDateTime paymentDate,
        PaymentStatus status
) { }
