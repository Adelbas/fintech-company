package com.academy.fintech.pg.core.service.merchant_provider.client;

import com.academy.fintech.pg.public_interface.merchant_provider.dto.PaymentResponseDto;
import com.academy.fintech.pg.public_interface.merchant_provider.dto.StatusCheckResponse;
import com.academy.fintech.pg.public_interface.payment.dto.PaymentRequestDto;
import org.mapstruct.Mapper;
import com.academy.fintech.model.PaymentDisbursementRequest;
import com.academy.fintech.model.PaymentResponse;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface MerchantProviderRestMapper {

    PaymentDisbursementRequest toPaymentDisbursementRequest (PaymentRequestDto paymentRequest);

    @Mapping(target = "statusCheckId", source = "paymentId")
    PaymentResponseDto toPaymentResponseDto (PaymentResponse paymentResponse);

    StatusCheckResponse toStatusCheckResponse(com.academy.fintech.model.StatusCheckResponse statusCheckResponse);

    default LocalDateTime map(OffsetDateTime offsetDateTime) {
        return offsetDateTime.toLocalDateTime();
    }
}
