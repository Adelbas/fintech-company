package com.academy.fintech.origination.core.service.export_task.application;

import com.academy.fintech.origination.core.service.application.db.application.entity.enums.ApplicationStatus;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record ApplicationStatusUpdatePayload(
        UUID applicationId,
        ApplicationStatus status,
        LocalDateTime updatedAt,
        UUID idempotencyKey
) { }
