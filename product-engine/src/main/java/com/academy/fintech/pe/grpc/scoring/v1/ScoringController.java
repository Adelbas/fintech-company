package com.academy.fintech.pe.grpc.scoring.v1;

import com.academy.fintech.pe.core.service.agreement.ScoringService;
import com.academy.fintech.pe.public_interface.scoring.dto.ScoringDataRequestDto;
import com.academy.fintech.pe.public_interface.scoring.dto.ScoringDataResponseDto;
import com.academy.fintech.scoring.ScoringDataRequest;
import com.academy.fintech.scoring.ScoringDataResponse;
import com.academy.fintech.scoring.ScoringDataServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;

/**
 * Scoring grpc controller that provides scoring data retrieval.
 * Uses {@link ScoringService} to perform logic.
 * Uses {@link ScoringGrpcMapper} to map grpc request to DTO and vice versa.
 */
@Slf4j
@GRpcService
@RequiredArgsConstructor
public class ScoringController extends ScoringDataServiceGrpc.ScoringDataServiceImplBase {

    private final ScoringService scoringService;

    private final ScoringGrpcMapper scoringGrpcMapper;

    @Override
    public void getScoringData(ScoringDataRequest request, StreamObserver<ScoringDataResponse> responseObserver) {
        ScoringDataRequestDto scoringDataRequestDto = scoringGrpcMapper.toScoringDataRequestDto(request);

        ScoringDataResponseDto scoringDataResponseDto = scoringService.getScoringData(scoringDataRequestDto);

        responseObserver.onNext(scoringGrpcMapper.toScoringDataResponse(scoringDataResponseDto));
        responseObserver.onCompleted();
    }
}
