package com.academy.fintech.pg.grpc.payment.v1;

import com.academy.fintech.pg.PaymentGateServiceGrpc;
import com.academy.fintech.pg.PaymentRequest;
import com.academy.fintech.pg.public_interface.payment.PaymentService;
import com.academy.fintech.pg.public_interface.payment.dto.PaymentRequestDto;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;

@Slf4j
@GRpcService
@RequiredArgsConstructor
public class PaymentController extends PaymentGateServiceGrpc.PaymentGateServiceImplBase {

    private final PaymentService paymentService;

    private final PaymentGrpcMapper paymentGrpcMapper;


    @Override
    public void disburseAmount(PaymentRequest request, StreamObserver<Empty> responseObserver) {
        PaymentRequestDto paymentRequestDto = paymentGrpcMapper.toPaymentRequestDto(request);

        log.info("Got disbursement request: {}", paymentRequestDto);

        paymentService.sendMoney(paymentRequestDto);
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }
}
