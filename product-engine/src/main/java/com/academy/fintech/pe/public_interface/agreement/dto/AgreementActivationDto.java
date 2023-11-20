package com.academy.fintech.pe.public_interface.agreement.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record AgreementActivationDto(
        UUID agreementNumber,
        LocalDateTime disbursementDate
) { }