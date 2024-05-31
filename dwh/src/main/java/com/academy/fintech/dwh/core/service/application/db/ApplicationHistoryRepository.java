package com.academy.fintech.dwh.core.service.application.db;

import com.academy.fintech.dwh.core.service.application.db.entity.ApplicationHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface ApplicationHistoryRepository extends JpaRepository<ApplicationHistory, Long> {
    Optional<ApplicationHistory> findAgreementHistoriesByBusinessDateAndIdempotencyKey(LocalDateTime date, UUID idempotencyKey);
}
