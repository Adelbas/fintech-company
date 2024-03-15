package com.academy.fintech.mp.rest;

import com.academy.fintech.mp.model.LoanPaymentRequest;
import com.academy.fintech.mp.model.PaymentDisbursementRequest;
import com.academy.fintech.mp.model.StatusCheckResponse;
import com.academy.fintech.mp.public_interface.payment.dto.DisbursementRequestDto;
import com.academy.fintech.mp.public_interface.payment.dto.LoanPaymentRequestDto;
import com.academy.fintech.mp.public_interface.payment.dto.StatusCheckResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.TimeZone;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface PaymentMapper {
    DisbursementRequestDto toDisbursementRequestDto(PaymentDisbursementRequest paymentDisbursementRequest);

    StatusCheckResponse toStatusCheckResponse(StatusCheckResponseDto statusCheckResponseDto);

    LoanPaymentRequestDto toLoanPaymentRequestDto(LoanPaymentRequest loanPaymentRequest);

    default OffsetDateTime map(LocalDateTime localDateTime) {
        ZonedDateTime zonedDateTime = localDateTime.atZone(TimeZone.getDefault().toZoneId());
        return zonedDateTime.toOffsetDateTime();
    }
}
