package com.academy.fintech.scoring.core.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record ScoringDataRequestDto(
        UUID clientId,
        String productCode,
        int loanTerm,
        BigDecimal disbursementAmount,
        BigDecimal originationAmount,
        BigDecimal interest
) { }
