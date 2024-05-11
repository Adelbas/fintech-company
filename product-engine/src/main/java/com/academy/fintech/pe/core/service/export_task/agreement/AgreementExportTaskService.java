package com.academy.fintech.pe.core.service.export_task.agreement;

import com.academy.fintech.exporter.enums.TaskStatus;
import com.academy.fintech.exporter.enums.TaskType;
import com.academy.fintech.exporter.export_task.db.ExportTaskRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AgreementExportTaskService {

    private final ExportTaskRepository exportTaskRepository;

    private final ObjectMapper objectMapper;

    @SneakyThrows
    public void save(UUID agreementNumber, AgreementExportStatus status) {
        AgreementStatusUpdatePayload payload = AgreementStatusUpdatePayload.builder()
                .agreementNumber(agreementNumber)
                .status(status)
                .updatedAt(LocalDateTime.now())
                .build();

        exportTaskRepository.saveTask(
                TaskType.AGREEMENT_STATUS_UPDATE,
                TaskStatus.NEW,
                agreementNumber.toString(),
                objectMapper.writeValueAsString(payload)
        );
    }
}
