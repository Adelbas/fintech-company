package com.academy.fintech.scoring.public_interface;

import com.academy.fintech.scoring.core.dto.ScoringDataRequestDto;
import com.academy.fintech.scoring.core.dto.ScoringDataResponseDto;

/**
 * Interface that provides scoring data retrieval
 */
public interface ScoringDataService {
    ScoringDataResponseDto getScoringData(ScoringDataRequestDto scoringDataRequestDto);
}
