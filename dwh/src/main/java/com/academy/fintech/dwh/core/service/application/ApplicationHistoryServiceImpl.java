package com.academy.fintech.dwh.core.service.application;

import com.academy.fintech.dwh.core.service.application.db.ApplicationHistoryDbService;
import com.academy.fintech.dwh.core.service.application.db.entity.ApplicationHistory;
import com.academy.fintech.dwh.public_interface.application.ApplicationHistoryService;
import com.academy.fintech.dwh.public_interface.application.dto.ApplicationStatusUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApplicationHistoryServiceImpl implements ApplicationHistoryService {

    private final ApplicationHistoryDbService applicationHistoryDbService;

    @Override
    public void handleApplicationStatusUpdate(ApplicationStatusUpdateDto applicationStatusUpdateDto) {
        if (isHandledBefore(applicationStatusUpdateDto.updatedAt(), applicationStatusUpdateDto.idempotencyKey())) {
            return;
        }

        applicationHistoryDbService.save(
                ApplicationHistory.builder()
                        .applicationId(applicationStatusUpdateDto.applicationId())
                        .status(applicationStatusUpdateDto.status())
                        .businessDate(applicationStatusUpdateDto.updatedAt())
                        .idempotencyKey(applicationStatusUpdateDto.idempotencyKey())
                        .build()
        );
    }

    private boolean isHandledBefore(LocalDateTime date, UUID idempotencyKey) {
        return applicationHistoryDbService.findDuplicateRecord(date, idempotencyKey).isPresent();
    }
}
