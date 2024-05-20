package com.academy.fintech.pg.core.service.payment.client.origination.grpc;

import com.academy.fintech.pg.DisbursementStatusRequest;
import com.academy.fintech.pg.PaymentGateServiceGrpc;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OriginationGrpcClient {

    private final PaymentGateServiceGrpc.PaymentGateServiceBlockingStub stub;

    public OriginationGrpcClient(OriginationGrpcClientProperty property) {
        Channel channel = ManagedChannelBuilder.forAddress(property.host(), property.port()).usePlaintext().build();
        this.stub = PaymentGateServiceGrpc.newBlockingStub(channel);
    }

    public void updateDisbursementStatus(DisbursementStatusRequest disbursementStatusRequest) {
        try {
            stub.updateDisbursementStatus(disbursementStatusRequest);
        } catch (StatusRuntimeException e) {
            log.error("Got error from Origination by request: {}", disbursementStatusRequest, e);
            throw e;
        }
    }

}
