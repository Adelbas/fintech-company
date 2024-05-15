package com.academy.fintech.dwh.core.service.application.db;

import com.academy.fintech.dwh.core.service.application.db.entity.ApplicationHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApplicationHistoryDbService {

    private final ApplicationHistoryRepository applicationHistoryRepository;

    public Optional<ApplicationHistory> findDuplicateRecord(LocalDateTime date, UUID idempotencyKey) {
        return applicationHistoryRepository.findAgreementHistoriesByBusinessDateAndIdempotencyKey(date, idempotencyKey);
    }

    public void save(ApplicationHistory applicationHistory) {
        applicationHistoryRepository.save(applicationHistory);
    }
}
