package com.academy.fintech.scoring.core.service;

import com.academy.fintech.scoring.core.dto.ScoringDataRequestDto;
import com.academy.fintech.scoring.core.dto.ScoringDataResponseDto;

/**
 * Interface that provides scoring data retrieval
 */
public interface ScoringDataService {
    ScoringDataResponseDto getScoringData(ScoringDataRequestDto scoringDataRequestDto);
}
