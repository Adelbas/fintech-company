package com.academy.fintech.origination.public_interface.scoring;

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