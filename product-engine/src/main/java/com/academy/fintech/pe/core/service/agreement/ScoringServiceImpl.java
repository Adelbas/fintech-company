package com.academy.fintech.pe.core.service.agreement;

import com.academy.fintech.pe.core.calculation.payment_schedule.PaymentScheduleFunctions;
import com.academy.fintech.pe.core.service.agreement.db.agreement.AgreementService;
import com.academy.fintech.pe.core.service.agreement.db.agreement.entity.Agreement;
import com.academy.fintech.pe.core.service.agreement.db.agreement.entity.enums.AgreementStatus;
import com.academy.fintech.pe.public_interface.agreement.AgreementMapper;
import com.academy.fintech.pe.public_interface.scoring.dto.ScoringDataRequestDto;
import com.academy.fintech.pe.public_interface.scoring.dto.ScoringDataResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents scoring service implementation.
 * Uses {@link AgreementService} to interact with database.
 * Uses {@link AgreementMapper} to map entity to DTO.
 */
@Service
@RequiredArgsConstructor
public class ScoringServiceImpl implements ScoringService{

    private final AgreementService agreementService;

    private final AgreementMapper agreementMapper;

    @Override
    @Transactional
    public ScoringDataResponseDto getScoringData(ScoringDataRequestDto scoringDataRequestDto) {
        BigDecimal periodPayment = PaymentScheduleFunctions.calculatePMT(
                scoringDataRequestDto.disbursementAmount().add(scoringDataRequestDto.originationAmount()),
                scoringDataRequestDto.interest(),
                scoringDataRequestDto.loanTerm()
        );

        List<Agreement> activeAgreements = agreementService
                .findAgreementsByClientIdAndStatus(scoringDataRequestDto.clientId(), AgreementStatus.ACTIVE)
                .orElse(new ArrayList<>());

        return ScoringDataResponseDto.builder()
                .periodPayment(periodPayment)
                .activeAgreements(agreementMapper.toAgreementResponseDtoList(activeAgreements))
                .build();
    }
}
