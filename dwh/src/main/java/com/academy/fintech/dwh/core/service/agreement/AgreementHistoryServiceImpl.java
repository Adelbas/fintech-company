package com.academy.fintech.dwh.core.service.agreement;

import com.academy.fintech.dwh.core.service.agreement.db.AgreementHistoryDbService;
import com.academy.fintech.dwh.core.service.agreement.db.entity.AgreementHistory;
import com.academy.fintech.dwh.public_interface.agreement.AgreementHistoryService;
import com.academy.fintech.dwh.public_interface.agreement.dto.AgreementStatusUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AgreementHistoryServiceImpl implements AgreementHistoryService {

    private final AgreementHistoryDbService agreementHistoryDbService;

    @Override
    public void handleAgreementStatusUpdate(AgreementStatusUpdateDto agreementStatusUpdateDto) {
        if (isHandledBefore(agreementStatusUpdateDto.updatedAt(), agreementStatusUpdateDto.idempotencyKey())) {
            return;
        }

        agreementHistoryDbService.save(
                AgreementHistory.builder()
                        .agreementNumber(agreementStatusUpdateDto.agreementNumber())
                        .status(agreementStatusUpdateDto.status())
                        .businessDate(agreementStatusUpdateDto.updatedAt())
                        .idempotencyKey(agreementStatusUpdateDto.idempotencyKey())
                        .build()
        );
    }

    private boolean isHandledBefore(LocalDateTime date, UUID idempotencyKey) {
        return agreementHistoryDbService.findDuplicateRecord(date, idempotencyKey).isPresent();
    }
}
