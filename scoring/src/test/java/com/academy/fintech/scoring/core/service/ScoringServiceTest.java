package com.academy.fintech.scoring.core.service;

import com.academy.fintech.scoring.core.dto.AgreementResponseDto;
import com.academy.fintech.scoring.core.dto.PaymentScheduleDto;
import com.academy.fintech.scoring.core.dto.PaymentSchedulePaymentDto;
import com.academy.fintech.scoring.core.dto.PaymentStatus;
import com.academy.fintech.scoring.core.dto.ScoringDataRequestDto;
import com.academy.fintech.scoring.core.dto.ScoringDataResponseDto;
import com.academy.fintech.scoring.public_interface.ScoringDataService;
import com.academy.fintech.scoring.public_interface.dto.ScoringRequestDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ScoringServiceTest {

    @Mock
    private ScoringDataService scoringDataService;

    @InjectMocks
    private ScoringServiceImpl scoringService;

    @Test
    void getScore_whenSalaryIsValidAndNoAgreements() {
        int expectedScore = 2;
        BigDecimal salary = BigDecimal.valueOf(200000);
        ScoringRequestDto scoringRequest = ScoringRequestDto.builder()
                .clientId(UUID.randomUUID())
                .salary(salary)
                .productCode("CL_1.0")
                .loanTerm(12)
                .interest(BigDecimal.valueOf(10))
                .disbursementAmount(BigDecimal.valueOf(300000))
                .originationAmount(BigDecimal.valueOf(10000))
                .build();
        ScoringDataResponseDto scoringDataResponseDto = ScoringDataResponseDto.builder()
                .periodPayment(BigDecimal.valueOf(40000))
                .activeAgreements(new ArrayList<>())
                .build();
        when(scoringDataService.getScoringData(any(ScoringDataRequestDto.class))).thenReturn(scoringDataResponseDto);

        int actualScore = scoringService.getScore(scoringRequest);

        assertThat(actualScore).isEqualTo(expectedScore);
    }

    @Test
    void getScore_whenSalaryInvalidAndNoAgreements() {
        int expectedScore = 1;
        BigDecimal salary = BigDecimal.valueOf(90000);
        ScoringRequestDto scoringRequest = ScoringRequestDto.builder()
                .clientId(UUID.randomUUID())
                .salary(salary)
                .productCode("CL_1.0")
                .loanTerm(12)
                .interest(BigDecimal.valueOf(10))
                .disbursementAmount(BigDecimal.valueOf(300000))
                .originationAmount(BigDecimal.valueOf(10000))
                .build();
        ScoringDataResponseDto scoringDataResponseDto = ScoringDataResponseDto.builder()
                .periodPayment(BigDecimal.valueOf(30001))
                .activeAgreements(new ArrayList<>())
                .build();
        when(scoringDataService.getScoringData(any(ScoringDataRequestDto.class))).thenReturn(scoringDataResponseDto);

        int actualScore = scoringService.getScore(scoringRequest);

        assertThat(actualScore).isEqualTo(expectedScore);
    }

    @Test
    void getScore_whenSalaryIsValidAndNoOverdue() {
        int expectedScore = 2;
        BigDecimal salary = BigDecimal.valueOf(200000);
        ScoringRequestDto scoringRequest = ScoringRequestDto.builder()
                .clientId(UUID.randomUUID())
                .salary(salary)
                .productCode("CL_1.0")
                .loanTerm(12)
                .interest(BigDecimal.valueOf(10))
                .disbursementAmount(BigDecimal.valueOf(300000))
                .originationAmount(BigDecimal.valueOf(10000))
                .build();
        List<PaymentSchedulePaymentDto> payments = new ArrayList<>();
        for (int i = 0; i < scoringRequest.loanTerm(); i++) {
            payments.add(
                    PaymentSchedulePaymentDto.builder()
                            .status(PaymentStatus.FUTURE)
                            .build()
            );
        }
        PaymentScheduleDto paymentScheduleDto = PaymentScheduleDto.builder()
                .payments(payments)
                .build();
        ScoringDataResponseDto scoringDataResponseDto = ScoringDataResponseDto.builder()
                .periodPayment(BigDecimal.valueOf(40000))
                .activeAgreements(List.of(AgreementResponseDto.builder().paymentSchedules(List.of(paymentScheduleDto)).build()))
                .build();
        when(scoringDataService.getScoringData(any(ScoringDataRequestDto.class))).thenReturn(scoringDataResponseDto);

        int actualScore = scoringService.getScore(scoringRequest);

        assertThat(actualScore).isEqualTo(expectedScore);
    }

    @Test
    void getScore_whenSalaryIsValidAndOverdueOf5Days() {
        int expectedScore = 1;
        BigDecimal salary = BigDecimal.valueOf(200000);
        ScoringRequestDto scoringRequest = ScoringRequestDto.builder()
                .clientId(UUID.randomUUID())
                .salary(salary)
                .productCode("CL_1.0")
                .loanTerm(12)
                .interest(BigDecimal.valueOf(10))
                .disbursementAmount(BigDecimal.valueOf(300000))
                .originationAmount(BigDecimal.valueOf(10000))
                .build();
        List<PaymentSchedulePaymentDto> payments = new ArrayList<>();
        for (int i = 0; i < scoringRequest.loanTerm(); i++) {
            payments.add(
                    PaymentSchedulePaymentDto.builder()
                            .status(PaymentStatus.FUTURE)
                            .build()
            );
        }
        payments.add(
                PaymentSchedulePaymentDto.builder()
                        .status(PaymentStatus.OVERDUE)
                        .paymentDate(LocalDateTime.now().minusDays(5))
                        .build()
        );
        PaymentScheduleDto paymentScheduleDto = PaymentScheduleDto.builder()
                .payments(payments)
                .build();
        ScoringDataResponseDto scoringDataResponseDto = ScoringDataResponseDto.builder()
                .periodPayment(BigDecimal.valueOf(40000))
                .activeAgreements(List.of(AgreementResponseDto.builder().paymentSchedules(List.of(paymentScheduleDto)).build()))
                .build();
        when(scoringDataService.getScoringData(any(ScoringDataRequestDto.class))).thenReturn(scoringDataResponseDto);

        int actualScore = scoringService.getScore(scoringRequest);

        assertThat(actualScore).isEqualTo(expectedScore);
    }

    @Test
    void getScore_whenSalaryInvalidAndOverdueOf5Days() {
        int expectedScore = 0;
        BigDecimal salary = BigDecimal.valueOf(10000);
        ScoringRequestDto scoringRequest = ScoringRequestDto.builder()
                .clientId(UUID.randomUUID())
                .salary(salary)
                .productCode("CL_1.0")
                .loanTerm(12)
                .interest(BigDecimal.valueOf(10))
                .disbursementAmount(BigDecimal.valueOf(300000))
                .originationAmount(BigDecimal.valueOf(10000))
                .build();
        List<PaymentSchedulePaymentDto> payments = new ArrayList<>();
        for (int i = 0; i < scoringRequest.loanTerm(); i++) {
            payments.add(
                    PaymentSchedulePaymentDto.builder()
                            .status(PaymentStatus.FUTURE)
                            .build()
            );
        }
        payments.add(
                PaymentSchedulePaymentDto.builder()
                        .status(PaymentStatus.OVERDUE)
                        .paymentDate(LocalDateTime.now().minusDays(5))
                        .build()
        );
        PaymentScheduleDto paymentScheduleDto = PaymentScheduleDto.builder()
                .payments(payments)
                .build();
        ScoringDataResponseDto scoringDataResponseDto = ScoringDataResponseDto.builder()
                .periodPayment(BigDecimal.valueOf(40000))
                .activeAgreements(List.of(AgreementResponseDto.builder().paymentSchedules(List.of(paymentScheduleDto)).build()))
                .build();
        when(scoringDataService.getScoringData(any(ScoringDataRequestDto.class))).thenReturn(scoringDataResponseDto);

        int actualScore = scoringService.getScore(scoringRequest);

        assertThat(actualScore).isEqualTo(expectedScore);
    }

    @Test
    void getScore_whenSalaryIsValidAndOverdueOf7Days() {
        int expectedScore = 0;
        BigDecimal salary = BigDecimal.valueOf(200000);
        ScoringRequestDto scoringRequest = ScoringRequestDto.builder()
                .clientId(UUID.randomUUID())
                .salary(salary)
                .productCode("CL_1.0")
                .loanTerm(12)
                .interest(BigDecimal.valueOf(10))
                .disbursementAmount(BigDecimal.valueOf(300000))
                .originationAmount(BigDecimal.valueOf(10000))
                .build();
        List<PaymentSchedulePaymentDto> payments = new ArrayList<>();
        for (int i = 0; i < scoringRequest.loanTerm(); i++) {
            payments.add(
                    PaymentSchedulePaymentDto.builder()
                            .status(PaymentStatus.FUTURE)
                            .build()
            );
        }
        payments.add(
                PaymentSchedulePaymentDto.builder()
                        .status(PaymentStatus.OVERDUE)
                        .paymentDate(LocalDateTime.now().minusDays(7))
                        .build()
        );
        PaymentScheduleDto paymentScheduleDto = PaymentScheduleDto.builder()
                .payments(payments)
                .build();
        ScoringDataResponseDto scoringDataResponseDto = ScoringDataResponseDto.builder()
                .periodPayment(BigDecimal.valueOf(40000))
                .activeAgreements(List.of(AgreementResponseDto.builder().paymentSchedules(List.of(paymentScheduleDto)).build()))
                .build();
        when(scoringDataService.getScoringData(any(ScoringDataRequestDto.class))).thenReturn(scoringDataResponseDto);

        int actualScore = scoringService.getScore(scoringRequest);

        assertThat(actualScore).isEqualTo(expectedScore);
    }

    @Test
    void getScore_whenSalaryInvalidAndOverdueOf7Days() {
        int expectedScore = -1;
        BigDecimal salary = BigDecimal.valueOf(10000);
        ScoringRequestDto scoringRequest = ScoringRequestDto.builder()
                .clientId(UUID.randomUUID())
                .salary(salary)
                .productCode("CL_1.0")
                .loanTerm(12)
                .interest(BigDecimal.valueOf(10))
                .disbursementAmount(BigDecimal.valueOf(300000))
                .originationAmount(BigDecimal.valueOf(10000))
                .build();
        List<PaymentSchedulePaymentDto> payments = new ArrayList<>();
        for (int i = 0; i < scoringRequest.loanTerm(); i++) {
            payments.add(
                    PaymentSchedulePaymentDto.builder()
                            .status(PaymentStatus.FUTURE)
                            .build()
            );
        }
        payments.add(
                PaymentSchedulePaymentDto.builder()
                        .status(PaymentStatus.OVERDUE)
                        .paymentDate(LocalDateTime.now().minusDays(7))
                        .build()
        );
        PaymentScheduleDto paymentScheduleDto = PaymentScheduleDto.builder()
                .payments(payments)
                .build();
        ScoringDataResponseDto scoringDataResponseDto = ScoringDataResponseDto.builder()
                .periodPayment(BigDecimal.valueOf(40000))
                .activeAgreements(List.of(AgreementResponseDto.builder().paymentSchedules(List.of(paymentScheduleDto)).build()))
                .build();
        when(scoringDataService.getScoringData(any(ScoringDataRequestDto.class))).thenReturn(scoringDataResponseDto);

        int actualScore = scoringService.getScore(scoringRequest);

        assertThat(actualScore).isEqualTo(expectedScore);
    }
}