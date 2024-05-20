package com.academy.fintech.pg.grpc.payment.v1;

import com.academy.fintech.pg.PaymentRequest;
import com.academy.fintech.pg.public_interface.payment.dto.PaymentRequestDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentGrpcMapper {

    PaymentRequestDto toPaymentRequestDto(PaymentRequest paymentRequest);
}
