package com.academy.fintech.scoring.core.product_engine.client;

import com.academy.fintech.scoring.ScoringDataRequest;
import com.academy.fintech.scoring.ScoringDataResponse;
import com.academy.fintech.scoring.core.product_engine.client.grpc.ProductEngineGrpcClient;
import com.academy.fintech.scoring.core.dto.ScoringDataRequestDto;
import com.academy.fintech.scoring.core.dto.ScoringDataResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Represents service that process responses from {@link ProductEngineGrpcClient}.
 * Uses {@link ProductEngineGrpcMapper} to map DTO to grpcRequest
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductEngineClientService {

    private final ProductEngineGrpcClient productEngineGrpcClient;

    private final ProductEngineGrpcMapper productEngineGrpcMapper;

    public ScoringDataResponseDto getScoringData(ScoringDataRequestDto scoringDataRequestDto) {
        ScoringDataRequest scoringDataGrpcRequest = productEngineGrpcMapper.toScoringDataRequest(scoringDataRequestDto);

        ScoringDataResponse scoringDataResponse = productEngineGrpcClient.getScoringData(scoringDataGrpcRequest);

        return productEngineGrpcMapper.toScoringDataResponseDto(scoringDataResponse);
    }
}
