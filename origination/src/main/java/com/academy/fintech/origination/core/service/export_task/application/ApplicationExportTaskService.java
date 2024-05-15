package com.academy.fintech.origination.core.service.export_task.application;

import com.academy.fintech.exporter.enums.TaskStatus;
import com.academy.fintech.exporter.enums.TaskType;
import com.academy.fintech.exporter.export_task.db.ExportTaskRepository;
import com.academy.fintech.origination.core.service.application.db.application.entity.enums.ApplicationStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApplicationExportTaskService {

    private final ExportTaskRepository exportTaskRepository;

    private final ObjectMapper objectMapper;

    @SneakyThrows
    public void save(UUID applicationId, ApplicationStatus status) {
        ApplicationStatusUpdatePayload payload = ApplicationStatusUpdatePayload.builder()
                .applicationId(applicationId)
                .status(status)
                .updatedAt(LocalDateTime.now())
                .idempotencyKey(UUID.randomUUID())
                .build();

        exportTaskRepository.saveTask(
                TaskType.APPLICATION_STATUS_UPDATE,
                TaskStatus.NEW,
                applicationId.toString(),
                objectMapper.writeValueAsString(payload)
        );
    }
}
