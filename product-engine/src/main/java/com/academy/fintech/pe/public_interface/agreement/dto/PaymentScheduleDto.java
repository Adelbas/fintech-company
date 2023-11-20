package com.academy.fintech.pe.public_interface.agreement.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record PaymentScheduleDto(
        int version,
        List<PaymentSchedulePaymentDto> payments
) { }
