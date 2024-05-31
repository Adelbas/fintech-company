package com.academy.fintech.dwh.public_interface.agreement.dto;

import com.academy.fintech.dwh.core.service.agreement.db.entity.enums.AgreementStatus;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record AgreementStatusUpdateDto(
    UUID agreementNumber,
    AgreementStatus status,
    LocalDateTime updatedAt,
    UUID idempotencyKey
) { }
