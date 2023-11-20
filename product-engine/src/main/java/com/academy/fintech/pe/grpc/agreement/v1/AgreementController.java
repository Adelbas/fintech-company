package com.academy.fintech.pe.grpc.agreement.v1;

import com.academy.fintech.pe.*;
import com.academy.fintech.pe.core.service.agreement.AgreementCreationService;
import com.academy.fintech.pe.public_interface.agreement.dto.PaymentScheduleDto;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;

import java.util.UUID;

/**
 * Agreement grpc controller that provides creating and activating of agreement.
 * Uses {@link AgreementCreationService} to perform logic.
 * Uses {@link AgreementGrpcMapper} to map grpc request to DTO and vice versa.
 * Uses {@link AgreementExceptionHandler} to handle exceptions
 */
@Slf4j
@GRpcService
@RequiredArgsConstructor
public class AgreementController extends AgreementServiceGrpc.AgreementServiceImplBase {

    private final AgreementCreationService agreementCreationService;

    private final AgreementGrpcMapper agreementGrpcMapper;

    @Override
    public void createAgreement(AgreementRequest request, StreamObserver<AgreementResponse> responseObserver) {
        log.info("Handle create agreement request: {}", request);

        UUID agreementNumber = agreementCreationService.createAgreement(agreementGrpcMapper.toAgreementDto(request));
        AgreementResponse agreementResponse = AgreementResponse.newBuilder().setAgreementNumber(agreementNumber.toString()).build();

        responseObserver.onNext(agreementResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void activateAgreement(AgreementActivationRequest request, StreamObserver<PaymentScheduleResponse> responseObserver) {
        log.info("Handle disbursement request: {}", request);

        PaymentScheduleDto paymentScheduleDto = agreementCreationService.activateAgreement(agreementGrpcMapper.toAgreementActivationDto(request));
        PaymentScheduleResponse paymentScheduleResponse = agreementGrpcMapper.toPaymentScheduleResponse(paymentScheduleDto);

        responseObserver.onNext(paymentScheduleResponse);
        responseObserver.onCompleted();
    }
}
