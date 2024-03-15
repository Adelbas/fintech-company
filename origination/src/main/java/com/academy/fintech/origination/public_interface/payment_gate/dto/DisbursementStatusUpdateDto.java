package com.academy.fintech.origination.public_interface.payment_gate.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record DisbursementStatusUpdateDto(
        UUID agreementNumber,
        boolean isDisbursementCompletedSuccessfully,
        LocalDateTime disbursementDate
) {
}
