package com.academy.fintech.pe.public_interface.scoring.dto;

import com.academy.fintech.pe.public_interface.agreement.dto.AgreementResponseDto;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record ScoringDataResponseDto(
        BigDecimal periodPayment,
        List<AgreementResponseDto> activeAgreements
) { }