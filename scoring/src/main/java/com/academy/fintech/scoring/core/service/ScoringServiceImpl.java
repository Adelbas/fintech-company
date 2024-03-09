package com.academy.fintech.scoring.core.service;

import com.academy.fintech.scoring.core.dto.AgreementResponseDto;
import com.academy.fintech.scoring.core.dto.PaymentScheduleDto;
import com.academy.fintech.scoring.core.dto.PaymentSchedulePaymentDto;
import com.academy.fintech.scoring.core.dto.PaymentStatus;
import com.academy.fintech.scoring.core.dto.ScoringDataRequestDto;
import com.academy.fintech.scoring.core.dto.ScoringDataResponseDto;
import com.academy.fintech.scoring.public_interface.ScoringDataService;
import com.academy.fintech.scoring.public_interface.ScoringService;
import com.academy.fintech.scoring.public_interface.dto.ScoringRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Represents scoring service implementation.
 * Uses {@link ScoringDataService} to obtain data from different services.
 */
@Service
@RequiredArgsConstructor
public class ScoringServiceImpl implements ScoringService {

    private static final int OVERDUE_LIMIT_DAYS = 7;

    private final ScoringDataService scoringDataService;

    /**
     * Calculates score by comparing client salary with period payment and checking for overdue payments
     * @param scoringRequestDto DTO with application parameters
     * @return calculated score
     */
    @Override
    public int getScore(ScoringRequestDto scoringRequestDto) {
        int score = 0;
        ScoringDataResponseDto scoringData = scoringDataService.getScoringData(
                ScoringDataRequestDto.builder()
                        .clientId(scoringRequestDto.clientId())
                        .productCode(scoringRequestDto.productCode())
                        .originationAmount(scoringRequestDto.originationAmount())
                        .disbursementAmount(scoringRequestDto.disbursementAmount())
                        .interest(scoringRequestDto.interest())
                        .loanTerm(scoringRequestDto.loanTerm())
                        .build()
        );

        if (isValidSalary(scoringRequestDto.salary(), scoringData.periodPayment())) {
            score++;
        }

        if (hasOverdueMoreThanLimit(scoringData.activeAgreements())) {
            score--;
        } else if (hasNoOverdue(scoringData.activeAgreements())) {
            score++;
        }

        return score;
    }

    private boolean isValidSalary(BigDecimal salary, BigDecimal periodPayment) {
        return periodPayment.multiply(BigDecimal.valueOf(3)).compareTo(salary) <= 0;
    }

    private boolean hasOverdueMoreThanLimit(List<AgreementResponseDto> agreements) {
        if (agreements == null || agreements.isEmpty()) {
            return false;
        }
        for (AgreementResponseDto agreement : agreements) {
            PaymentScheduleDto paymentSchedule = agreement.paymentSchedules().get(agreement.paymentSchedules().size() - 1);
            for (PaymentSchedulePaymentDto payment : paymentSchedule.payments()) {
                if (PaymentStatus.OVERDUE.equals(payment.status())) {
                    LocalDate currentDay = LocalDate.now();
                    LocalDate paymentDate = payment.paymentDate().toLocalDate();
                    if (paymentDate.plusDays(OVERDUE_LIMIT_DAYS).isEqual(currentDay) || (paymentDate.plusDays(OVERDUE_LIMIT_DAYS).isBefore(currentDay))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean hasNoOverdue(List<AgreementResponseDto> agreements) {
        if (agreements == null || agreements.isEmpty()) {
            return true;
        }
        for (AgreementResponseDto agreement : agreements) {
            PaymentScheduleDto paymentSchedule = agreement.paymentSchedules().get(agreement.paymentSchedules().size() - 1);
            for (PaymentSchedulePaymentDto payment : paymentSchedule.payments()) {
                if (PaymentStatus.OVERDUE.equals(payment.status())) {
                    return false;
                }
            }
        }
        return true;
    }
}
