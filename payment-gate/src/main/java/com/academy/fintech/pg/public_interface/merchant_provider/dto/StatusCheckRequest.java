package com.academy.fintech.pg.public_interface.merchant_provider.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record StatusCheckRequest(
    UUID statusCheckId
) { }
