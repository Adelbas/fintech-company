package com.academy.fintech.pe.public_interface.agreement.dto;

import com.academy.fintech.pe.public_interface.agreement.dto.PaymentScheduleDto;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
public record AgreementResponseDto(
        UUID agreementNumber,
        String productCode,
        int loanTerm,
        BigDecimal principalAmount,
        BigDecimal originationAmount,
        BigDecimal interest,
        LocalDateTime disbursementDate,
        LocalDateTime nextPaymentDate,
        List<PaymentScheduleDto> paymentSchedules
) { }
