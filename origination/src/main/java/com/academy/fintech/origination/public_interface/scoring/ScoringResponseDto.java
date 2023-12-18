package com.academy.fintech.origination.public_interface.scoring;

import lombok.Builder;

@Builder
public record ScoringResponseDto(
        int score
) { }
