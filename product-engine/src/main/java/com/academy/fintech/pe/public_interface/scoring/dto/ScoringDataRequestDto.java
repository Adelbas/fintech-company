package com.academy.fintech.pe.public_interface.scoring.dto;

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