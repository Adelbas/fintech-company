package com.academy.fintech.origination.public_interface.application.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record ApplicationEmailDto(
        UUID applicationId,
        String email,
        String firstName,
        String lastName
) { }
