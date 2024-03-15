package com.academy.fintech.origination.public_interface.agreement.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record AgreementDto(
        UUID clientId,
        String productCode,
        int loanTerm,
        BigDecimal disbursementAmount,
        BigDecimal originationAmount,
        BigDecimal interest
) { }
