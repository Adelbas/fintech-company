package com.academy.fintech.origination.grpc.application.v1;

import com.academy.fintech.application.ApplicationRequest;
import com.academy.fintech.application.ApplicationResponse;
import com.academy.fintech.application.ApplicationServiceGrpc;
import com.academy.fintech.application.CancelApplicationRequest;
import com.academy.fintech.application.CancelApplicationResponse;
import com.academy.fintech.origination.public_interface.application.ApplicationService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;

import java.util.UUID;

/**
 * Application grpc controller that provides creating and canceling of application.
 * Uses {@link ApplicationService} to perform logic.
 * Uses {@link ApplicationGrpcMapper} to map grpc request to DTO and vice versa.
 * Uses {@link ApplicationExceptionHandler} to handle exceptions
 */
@Slf4j
@GRpcService
@RequiredArgsConstructor
public class ApplicationController extends ApplicationServiceGrpc.ApplicationServiceImplBase {

    private final ApplicationService applicationService;

    private final ApplicationGrpcMapper applicationGrpcMapper;

    @Override
    public void create(ApplicationRequest request, StreamObserver<ApplicationResponse> responseObserver) {
        UUID applicationId = applicationService.createApplication(applicationGrpcMapper.toApplicationDto(request));

        responseObserver.onNext(
                ApplicationResponse.newBuilder()
                        .setApplicationId(applicationId.toString())
                        .build()
        );
        responseObserver.onCompleted();
    }

    @Override
    public void cancelApplication(CancelApplicationRequest request, StreamObserver<CancelApplicationResponse> responseObserver) {
        boolean isCanceled = applicationService.cancelApplication(applicationGrpcMapper.toCancelApplicationDto(request));

        responseObserver.onNext(
                CancelApplicationResponse.newBuilder()
                        .setIsCanceled(isCanceled)
                        .build()
        );
        responseObserver.onCompleted();
    }
}
