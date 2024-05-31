package com.academy.fintech.dwh.core.service.agreement.db;

import com.academy.fintech.dwh.core.service.agreement.db.entity.AgreementHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AgreementHistoryRepository extends JpaRepository<AgreementHistory, Long> {
    Optional<AgreementHistory> findAgreementHistoriesByBusinessDateAndIdempotencyKey(LocalDateTime date, UUID idempotencyKey);
}
