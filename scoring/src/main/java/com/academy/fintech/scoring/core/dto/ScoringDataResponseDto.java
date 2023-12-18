package com.academy.fintech.scoring.core.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record ScoringDataResponseDto(
        BigDecimal periodPayment,
        List<AgreementResponseDto> activeAgreements
) { }