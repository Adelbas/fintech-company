package com.academy.fintech.scoring.core.service;

import com.academy.fintech.scoring.core.product_engine.client.ProductEngineClientService;
import com.academy.fintech.scoring.core.dto.ScoringDataRequestDto;
import com.academy.fintech.scoring.core.dto.ScoringDataResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Represents scoring data service implementation.
 * Obtain data from Product-Engine microservice
 * Uses {@link ProductEngineClientService} to do calls to PE.
 */
@Service
@RequiredArgsConstructor
public class ScoringDataServiceImpl implements ScoringDataService {

    private final ProductEngineClientService productEngineClientService;

    @Override
    public ScoringDataResponseDto getScoringData(ScoringDataRequestDto scoringDataRequestDto) {
        return productEngineClientService.getScoringData(scoringDataRequestDto);
    }
}
