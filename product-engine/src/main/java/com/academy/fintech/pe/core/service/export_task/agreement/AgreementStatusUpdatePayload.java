package com.academy.fintech.pe.core.service.export_task.agreement;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record AgreementStatusUpdatePayload(
    UUID agreementNumber,
    AgreementExportStatus status,
    LocalDateTime updatedAt
) { }
