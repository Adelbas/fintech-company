package com.academy.fintech.pe.grpc.scoring.v1;

import com.academy.fintech.pe.public_interface.agreement.dto.AgreementResponseDto;
import com.academy.fintech.pe.public_interface.agreement.dto.PaymentScheduleDto;
import com.academy.fintech.pe.public_interface.scoring.dto.ScoringDataRequestDto;
import com.academy.fintech.pe.public_interface.scoring.dto.ScoringDataResponseDto;
import com.academy.fintech.scoring.AgreementResponse;
import com.academy.fintech.scoring.ScoringDataRequest;
import com.academy.fintech.scoring.ScoringDataResponse;
import com.academy.fintech.scoring.PaymentScheduleResponse;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface ScoringGrpcMapper {

    @Mapping(target = "paymentsList", source = "payments")
    PaymentScheduleResponse toPaymentScheduleResponse(PaymentScheduleDto paymentScheduleDto);

    @Mapping(target = "paymentSchedulesList", source = "paymentSchedules")
    AgreementResponse toAgreementResponse(AgreementResponseDto agreementResponseDto);

    @Mapping(target = "activeAgreementsList", source = "activeAgreements")
    ScoringDataResponse toScoringDataResponse(ScoringDataResponseDto scoringDataResponseDto);

    ScoringDataRequestDto toScoringDataRequestDto(ScoringDataRequest scoringDataRequest);
}
