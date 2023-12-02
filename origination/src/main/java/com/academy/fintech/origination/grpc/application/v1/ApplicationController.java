package com.academy.fintech.origination.grpc.application.v1;

import com.academy.fintech.application.*;
import com.academy.fintech.origination.core.service.application.ApplicationCreationService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;

import java.util.UUID;

/**
 * Application grpc controller that provides creating and canceling of application.
 * Uses {@link ApplicationCreationService} to perform logic.
 * Uses {@link ApplicationGrpcMapper} to map grpc request to DTO and vice versa.
 * Uses {@link ApplicationExceptionHandler} to handle exceptions
 */
@Slf4j
@GRpcService
@RequiredArgsConstructor
public class ApplicationController extends ApplicationServiceGrpc.ApplicationServiceImplBase {

    private final ApplicationCreationService applicationCreationService;

    private final ApplicationGrpcMapper applicationGrpcMapper;

    @Override
    public void create(ApplicationRequest request, StreamObserver<ApplicationResponse> responseObserver) {
        UUID applicationId = applicationCreationService.createApplication(applicationGrpcMapper.toApplicationDto(request));

        responseObserver.onNext(
                ApplicationResponse.newBuilder()
                        .setApplicationId(applicationId.toString())
                        .build()
        );
        responseObserver.onCompleted();
    }

    @Override
    public void cancelApplication(CancelApplicationRequest request, StreamObserver<CancelApplicationResponse> responseObserver) {
        boolean isCanceled = applicationCreationService.cancelApplication(applicationGrpcMapper.toCancelApplicationDto(request));

        responseObserver.onNext(
                CancelApplicationResponse.newBuilder()
                        .setIsCanceled(isCanceled)
                        .build()
        );
        responseObserver.onCompleted();
    }
}
