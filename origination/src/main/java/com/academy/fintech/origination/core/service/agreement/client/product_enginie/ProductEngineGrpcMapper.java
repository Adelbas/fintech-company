package com.academy.fintech.origination.core.service.agreement.client.product_enginie;

import com.academy.fintech.origination.public_interface.agreement.dto.AgreementActivationDto;
import com.academy.fintech.origination.public_interface.agreement.dto.AgreementDto;
import com.academy.fintech.pe.AgreementActivationRequest;
import com.academy.fintech.pe.AgreementRequest;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface ProductEngineGrpcMapper {

    AgreementRequest toAgreementRequest(AgreementDto agreementDto);

    AgreementActivationDto toAgreementActivationDto(AgreementActivationRequest agreementActivationRequest);

    AgreementActivationRequest toAgreementActivationRequest(AgreementActivationDto agreementActivationDto);
}