package com.academy.fintech.api.core.origination.client;

import com.academy.fintech.api.core.origination.client.grpc.OriginationGrpcClient;
import com.academy.fintech.api.public_interface.application.dto.ApplicationDto;
import com.academy.fintech.application.ApplicationRequest;
import com.academy.fintech.application.ApplicationResponse;
import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Represents service that process responses from {@link OriginationGrpcClient}.
 * Uses {@link OriginationGrpcMapper} to map DTO to grpcRequest
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OriginationClientService {

    private final OriginationGrpcClient originationGrpcClient;

    private final OriginationGrpcMapper originationGrpcMapper;

    /**
     * Provides application creation.
     * Catches {@link StatusRuntimeException} with {@link Status.Code#ALREADY_EXISTS} and gets applicationId from grpc trailers {@link Metadata}.
     *
     * @param applicationDto applicationDto
     * @return created applicationId
     */
    public String createApplication(ApplicationDto applicationDto) {
        ApplicationRequest request = originationGrpcMapper.toApplicationRequest(applicationDto);

        ApplicationResponse response;
        try {
            response = originationGrpcClient.createApplication(request);
        } catch (StatusRuntimeException e) {
            if (e.getStatus().getCode().equals(Status.Code.ALREADY_EXISTS) && e.getTrailers() != null) {
                log.info("Handled exception {}", e.getMessage());
                return e.getTrailers().get(Metadata.Key.of("application_id", Metadata.ASCII_STRING_MARSHALLER));
            }
            throw e;
        }
        return response.getApplicationId();
    }
}
