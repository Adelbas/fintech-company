package com.academy.fintech.origination.core.service.agreement.client.product_enginie.grpc;

import com.academy.fintech.pe.AgreementRequest;
import com.academy.fintech.pe.AgreementResponse;
import com.academy.fintech.pe.AgreementServiceGrpc;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import com.academy.fintech.pe.AgreementServiceGrpc.AgreementServiceBlockingStub;

/**
 * Represents grpc client to interact with Product-Engine microservice.
 * Uses {@link ProductEngineGrpcClientProperty} to set properties for connection.
 * Uses {@link AgreementServiceBlockingStub} to do rpc calls.
 */
@Slf4j
@Component
public class ProductEngineGrpcClient {
    private final AgreementServiceBlockingStub stub;

    public ProductEngineGrpcClient(ProductEngineGrpcClientProperty property) {
        Channel channel = ManagedChannelBuilder.forAddress(property.host(), property.port()).usePlaintext().build();
        this.stub = AgreementServiceGrpc.newBlockingStub(channel);
    }

    public AgreementResponse createAgreement(AgreementRequest agreementRequest) {
        try {
            return stub.createAgreement(agreementRequest);
        } catch (StatusRuntimeException e) {
            log.error("Got error from Product-engine by request: {}", agreementRequest, e);
            throw e;
        }
    }
}
