package com.academy.fintech.scoring.grpc.origination.v1;

import com.academy.fintech.scoring.ScoringRequest;
import com.academy.fintech.scoring.ScoringResponse;
import com.academy.fintech.scoring.ScoringServiceGrpc;
import com.academy.fintech.scoring.public_interface.ScoringService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;

/**
 * Origination grpc controller that provides application scoring.
 * Uses {@link ScoringService} to perform logic.
 * Uses {@link OriginationGrpcMapper} to map grpc request to DTO and vice versa.
 */
@Slf4j
@GRpcService
@RequiredArgsConstructor
public class OriginationController extends ScoringServiceGrpc.ScoringServiceImplBase {

    private final ScoringService scoringService;

    private final OriginationGrpcMapper originationGrpcMapper;

    @Override
    public void scoreApplication(ScoringRequest request, StreamObserver<ScoringResponse> responseObserver) {
        int score = scoringService.getScore(originationGrpcMapper.toScoringRequestDto(request));

        ScoringResponse scoringResponse = ScoringResponse.newBuilder()
                .setScore(score)
                .build();

        responseObserver.onNext(scoringResponse);
        responseObserver.onCompleted();
    }
}
