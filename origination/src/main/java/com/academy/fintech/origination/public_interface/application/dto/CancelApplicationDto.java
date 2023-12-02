package com.academy.fintech.origination.public_interface.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.util.UUID;

@Builder
public record CancelApplicationDto(
        @NotBlank(message = "Empty applicationId")
        UUID applicationId
) { }
