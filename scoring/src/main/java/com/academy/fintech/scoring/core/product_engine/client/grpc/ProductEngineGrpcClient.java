package com.academy.fintech.scoring.core.product_engine.client.grpc;

import com.academy.fintech.scoring.ScoringDataRequest;
import com.academy.fintech.scoring.ScoringDataResponse;
import com.academy.fintech.scoring.ScoringDataServiceGrpc;
import com.academy.fintech.scoring.ScoringDataServiceGrpc.ScoringDataServiceBlockingStub;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Represents grpc client to interact with Product-Engine microservice.
 * Uses {@link ScoringDataServiceBlockingStub} to set properties for connection.
 * Uses {@link ScoringDataServiceBlockingStub} to do rpc calls.
 */
@Slf4j
@Component
public class ProductEngineGrpcClient {
    private final ScoringDataServiceBlockingStub stub;

    public ProductEngineGrpcClient(ProductEngineGrpcClientProperty property) {
        Channel channel = ManagedChannelBuilder.forAddress(property.host(), property.port()).usePlaintext().build();
        this.stub = ScoringDataServiceGrpc.newBlockingStub(channel);
    }

    public ScoringDataResponse getScoringData(ScoringDataRequest scoringDataRequest) {
        try {
            return stub.getScoringData(scoringDataRequest);
        } catch (StatusRuntimeException e) {
            log.error("Got error from Product-engine by request: {}", scoringDataRequest, e);
            throw e;
        }
    }
}
