package com.academy.fintech.origination.core.service.payment_gate.client.grpc;

import com.academy.fintech.pg.PaymentGateServiceGrpc;
import com.academy.fintech.pg.PaymentGateServiceGrpc.PaymentGateServiceBlockingStub;
import com.academy.fintech.pg.PaymentRequest;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PaymentGateGrpcClient {

    private final PaymentGateServiceBlockingStub stub;

    public PaymentGateGrpcClient(PaymentGateClientProperty property) {
        Channel channel = ManagedChannelBuilder.forAddress(property.host(), property.port()).usePlaintext().build();
        this.stub = PaymentGateServiceGrpc.newBlockingStub(channel);
    }

    public void disburseAmount(PaymentRequest paymentRequest) {
        try {
            stub.disburseAmount(paymentRequest);
        } catch (StatusRuntimeException e) {
            log.error("Got error from Payment-gate by request: {}", paymentRequest, e);
            throw e;
        }
    }
}