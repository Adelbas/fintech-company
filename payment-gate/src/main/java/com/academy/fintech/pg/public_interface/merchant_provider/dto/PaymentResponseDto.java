package com.academy.fintech.pg.public_interface.merchant_provider.dto;

import java.util.UUID;

public record PaymentResponseDto(
        UUID statusCheckId
)
{ }
