package com.academy.fintech.api.core.origination.client.grpc;

import com.academy.fintech.application.ApplicationRequest;
import com.academy.fintech.application.ApplicationResponse;
import com.academy.fintech.application.ApplicationServiceGrpc;
import com.academy.fintech.application.ApplicationServiceGrpc.ApplicationServiceBlockingStub;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Represents grpc client to interact with Origination microservice.
 * Uses {@link OriginationGrpcClientProperty} to set properties for connection.
 * Uses {@link ApplicationServiceBlockingStub} to do rpc calls.
 */
@Slf4j
@Component
public class OriginationGrpcClient {

    private final ApplicationServiceBlockingStub stub;

    public OriginationGrpcClient(OriginationGrpcClientProperty property) {
        Channel channel = ManagedChannelBuilder.forAddress(property.host(), property.port()).usePlaintext().build();
        this.stub = ApplicationServiceGrpc.newBlockingStub(channel);
    }

    public ApplicationResponse createApplication(ApplicationRequest applicationRequest) {
        try {
            return stub.create(applicationRequest);
        } catch (StatusRuntimeException e) {
            log.error("Got error from Origination by request: {}", applicationRequest, e);
            throw e;
        }
    }

}
