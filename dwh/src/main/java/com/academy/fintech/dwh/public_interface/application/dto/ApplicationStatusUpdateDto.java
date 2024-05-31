package com.academy.fintech.dwh.public_interface.application.dto;

import com.academy.fintech.dwh.core.service.application.db.entity.enums.ApplicationStatus;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record ApplicationStatusUpdateDto(
        UUID applicationId,
        ApplicationStatus status,
        LocalDateTime updatedAt,
        UUID idempotencyKey
) { }
