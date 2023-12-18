package com.academy.fintech.scoring.public_interface.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record ScoringRequestDto(
        UUID clientId,
        BigDecimal salary,
        String productCode,
        int loanTerm,
        BigDecimal disbursementAmount,
        BigDecimal originationAmount,
        BigDecimal interest
) { }