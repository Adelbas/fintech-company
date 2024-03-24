package com.academy.fintech.pg.core.service.payment.client.product_engine.grpc;

import com.academy.fintech.pe.AgreementServiceGrpc;
import com.academy.fintech.pe.LoanPaymentRequest;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProductEngineGrpcClient {

    private final AgreementServiceGrpc.AgreementServiceBlockingStub stub;

    public ProductEngineGrpcClient(ProductEngineGrpcClientProperty property) {
        Channel channel = ManagedChannelBuilder.forAddress(property.host(), property.port()).usePlaintext().build();
        this.stub = AgreementServiceGrpc.newBlockingStub(channel);
    }

    public void handleLoanPayment(LoanPaymentRequest loanPaymentRequest) {
        try {
            stub.handleLoanPayment(loanPaymentRequest);
        } catch (StatusRuntimeException e) {
            log.error("Got error from Product-engine by request: {}", loanPaymentRequest, e);
            throw e;
        }
    }
}
