package com.academy.fintech.origination.core.service.scoring.client;

import com.academy.fintech.origination.core.service.scoring.client.grpc.ScoringGrpcClient;
import com.academy.fintech.origination.public_interface.scoring.ScoringRequestDto;
import com.academy.fintech.origination.public_interface.scoring.ScoringResponseDto;
import com.academy.fintech.scoring.ScoringRequest;
import com.academy.fintech.scoring.ScoringResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Represents service that process responses from {@link ScoringGrpcClient}.
 * Uses {@link ScoringGrpcMapper} to map DTO to grpcRequest
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ScoringClientService {

    private final ScoringGrpcClient scoringGrpcClient;

    private final ScoringGrpcMapper scoringGrpcMapper;

    public ScoringResponseDto scoreApplication(ScoringRequestDto scoringRequestDto) {
        ScoringRequest request = scoringGrpcMapper.toScoringRequest(scoringRequestDto);

        ScoringResponse response = scoringGrpcClient.scoreApplication(request);

        return scoringGrpcMapper.toScoringResponse(response);
    }
}
