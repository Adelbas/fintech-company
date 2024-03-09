package com.academy.fintech.pe.public_interface.scoring;

import com.academy.fintech.pe.public_interface.scoring.dto.ScoringDataRequestDto;
import com.academy.fintech.pe.public_interface.scoring.dto.ScoringDataResponseDto;

/**
 * Interface that performs logic for scoring data retrieval
 */
public interface ScoringService {
    ScoringDataResponseDto getScoringData(ScoringDataRequestDto scoringDataRequestDto);
}
