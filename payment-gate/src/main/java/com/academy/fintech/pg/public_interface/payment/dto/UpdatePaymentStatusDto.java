package com.academy.fintech.pg.public_interface.payment.dto;

import com.academy.fintech.pg.core.service.payment.db.entity.enums.PaymentStatus;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record UpdatePaymentStatusDto(
      UUID agreementNumber,
      PaymentStatus status,
      LocalDateTime completionDate
) { }
