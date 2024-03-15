package com.academy.fintech.mp.public_interface.payment.dto;

import com.academy.fintech.mp.core.service.payment.db.entity.enums.PaymentStatus;
import lombok.Builder;

import java.util.UUID;

@Builder
public record PaymentStatusUpdateDto(
        UUID paymentId,
        PaymentStatus status
) { }
