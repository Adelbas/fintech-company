package com.academy.fintech.scoring.core.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record PaymentScheduleDto(
        int version,
        List<PaymentSchedulePaymentDto> payments
) { }