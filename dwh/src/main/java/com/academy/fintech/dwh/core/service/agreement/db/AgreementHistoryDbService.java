package com.academy.fintech.dwh.core.service.agreement.db;

import com.academy.fintech.dwh.core.service.agreement.db.AgreementHistoryRepository;
import com.academy.fintech.dwh.core.service.agreement.db.entity.AgreementHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AgreementHistoryDbService {

    private final AgreementHistoryRepository agreementHistoryRepository;

    public Optional<AgreementHistory> findDuplicateRecord(LocalDateTime date, UUID idempotencyKey) {
        return agreementHistoryRepository.findAgreementHistoriesByBusinessDateAndIdempotencyKey(date, idempotencyKey);
    }

    public void save(AgreementHistory agreementHistory) {
        agreementHistoryRepository.save(agreementHistory);
    }
}
