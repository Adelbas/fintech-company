package com.academy.fintech.pg.public_interface.merchant_provider.dto;

import com.academy.fintech.pg.core.service.payment.db.entity.enums.PaymentStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record StatusCheckResponse(
    PaymentStatus status,
    LocalDateTime completionDate
) { }
