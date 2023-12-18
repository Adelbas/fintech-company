package com.academy.fintech.scoring.core.service;

import com.academy.fintech.scoring.public_interface.dto.ScoringRequestDto;

/**
 * Interface that provides scoring
 */
public interface ScoringService {
    int getScore(ScoringRequestDto scoringRequestDto);
}
