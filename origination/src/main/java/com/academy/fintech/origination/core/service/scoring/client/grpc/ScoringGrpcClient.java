package com.academy.fintech.origination.core.service.scoring.client.grpc;

import com.academy.fintech.scoring.ScoringRequest;
import com.academy.fintech.scoring.ScoringResponse;
import com.academy.fintech.scoring.ScoringServiceGrpc;
import com.academy.fintech.scoring.ScoringServiceGrpc.ScoringServiceBlockingStub;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Represents grpc client to interact with Scoring microservice.
 * Uses {@link ScoringGrpcClientProperty} to set properties for connection.
 * Uses {@link ScoringServiceBlockingStub} to do rpc calls.
 */
@Slf4j
@Component
public class ScoringGrpcClient {

    private final ScoringServiceBlockingStub stub;

    public ScoringGrpcClient(ScoringGrpcClientProperty property) {
        Channel channel = ManagedChannelBuilder.forAddress(property.host(), property.port()).usePlaintext().build();
        this.stub = ScoringServiceGrpc.newBlockingStub(channel);
    }

    public ScoringResponse scoreApplication(ScoringRequest applicationRequest) {
        try {
            return stub.scoreApplication(applicationRequest);
        } catch (StatusRuntimeException e) {
            log.error("Got error from Scoring by request: {}", applicationRequest, e);
            throw e;
        }
    }
}
