package com.academy.fintech.origination.core.service.agreement.client.product_enginie;

import com.academy.fintech.origination.core.service.agreement.client.product_enginie.grpc.ProductEngineGrpcClient;
import com.academy.fintech.origination.public_interface.agreement.dto.AgreementActivationDto;
import com.academy.fintech.origination.public_interface.agreement.dto.AgreementDto;
import com.academy.fintech.pe.AgreementActivationRequest;
import com.academy.fintech.pe.AgreementRequest;
import com.academy.fintech.pe.AgreementResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Represents service that process responses from {@link ProductEngineGrpcClient}.
 * Uses {@link ProductEngineGrpcMapper} to map DTO to grpcRequest
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductEngineClientService {

    private final ProductEngineGrpcClient productEngineGrpcClient;

    private final ProductEngineGrpcMapper productEngineGrpcMapper;

    public UUID createAgreement(AgreementDto agreementDto) {
        log.info("Send request to Product-engine: {}", agreementDto);
        AgreementRequest agreementRequest = productEngineGrpcMapper.toAgreementRequest(agreementDto);

        AgreementResponse agreementResponse = productEngineGrpcClient.createAgreement(agreementRequest);
        log.info("Got response from Product-engine: {}", agreementResponse);

        return UUID.fromString(agreementResponse.getAgreementNumber());
    }

    public void activateAgreement(AgreementActivationDto agreementActivationDto) {
        log.info("Send agreement activation request to Product-engine: {}", agreementActivationDto);
        AgreementActivationRequest agreementActivationRequest = productEngineGrpcMapper.toAgreementActivationRequest(agreementActivationDto);

        productEngineGrpcClient.activateAgreement(agreementActivationRequest);
    }
}
