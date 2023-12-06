package com.academy.fintech.scoring.core.product_engine.client;

import com.academy.fintech.scoring.AgreementResponse;
import com.academy.fintech.scoring.PaymentSchedulePaymentResponse;
import com.academy.fintech.scoring.PaymentScheduleResponse;
import com.academy.fintech.scoring.ScoringDataRequest;
import com.academy.fintech.scoring.ScoringDataResponse;
import com.academy.fintech.scoring.core.dto.AgreementResponseDto;
import com.academy.fintech.scoring.core.dto.PaymentScheduleDto;
import com.academy.fintech.scoring.core.dto.PaymentSchedulePaymentDto;
import com.academy.fintech.scoring.core.dto.ScoringDataRequestDto;
import com.academy.fintech.scoring.core.dto.ScoringDataResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductEngineGrpcMapper {
    ScoringDataRequest toScoringDataRequest(ScoringDataRequestDto scoringDataRequestDto);

    PaymentSchedulePaymentDto toPaymentSchedulePaymentDto(PaymentSchedulePaymentResponse paymentSchedulePaymentResponse);

    @Mapping(target = "payments", source = "paymentsList")
    PaymentScheduleDto toPaymentScheduleDto(PaymentScheduleResponse paymentScheduleResponse);

    @Mapping(target = "paymentSchedules", source = "paymentSchedulesList")
    AgreementResponseDto toAgreementResponseDto(AgreementResponse agreementResponse);

    @Mapping(target = "activeAgreements", source = "activeAgreementsList")
    ScoringDataResponseDto toScoringDataResponseDto(ScoringDataResponse scoringDataResponse);
}
