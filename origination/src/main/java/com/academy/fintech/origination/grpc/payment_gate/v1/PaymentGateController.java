package com.academy.fintech.origination.grpc.payment_gate.v1;

import com.academy.fintech.origination.public_interface.agreement.AgreementService;
import com.academy.fintech.pg.DisbursementStatusRequest;
import com.academy.fintech.pg.PaymentGateServiceGrpc;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;

@Slf4j
@GRpcService
@RequiredArgsConstructor
public class PaymentGateController extends PaymentGateServiceGrpc.PaymentGateServiceImplBase {

    private final PaymentGateMapper paymentGateMapper;

    private final AgreementService agreementService;

    @Override
    public void updateDisbursementStatus(DisbursementStatusRequest request, StreamObserver<Empty> responseObserver) {
        log.info("Got updateDisbursementStatus request from Payment-gate: {}", request);
        agreementService.updateDisbursementStatus(paymentGateMapper.toDisbursementStatusUpdateDto(request));

        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }
}
