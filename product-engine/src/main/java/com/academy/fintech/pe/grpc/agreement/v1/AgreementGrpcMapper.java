package com.academy.fintech.pe.grpc.agreement.v1;

import com.academy.fintech.pe.AgreementActivationRequest;
import com.academy.fintech.pe.AgreementRequest;
import com.academy.fintech.pe.PaymentScheduleResponse;
import com.academy.fintech.pe.public_interface.agreement.dto.AgreementActivationDto;
import com.academy.fintech.pe.public_interface.agreement.dto.AgreementDto;
import com.academy.fintech.pe.public_interface.agreement.dto.PaymentScheduleDto;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface AgreementGrpcMapper {

    AgreementDto toAgreementDto(AgreementRequest agreementRequest);

    AgreementActivationDto toAgreementActivationDto(AgreementActivationRequest agreementActivationRequest);

    @Mapping(target = "paymentsList", source = "payments")
    PaymentScheduleResponse toPaymentScheduleResponse(PaymentScheduleDto paymentScheduleDto);
}