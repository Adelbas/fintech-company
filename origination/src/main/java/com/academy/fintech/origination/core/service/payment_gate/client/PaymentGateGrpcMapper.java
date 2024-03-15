package com.academy.fintech.origination.core.service.payment_gate.client;

import com.academy.fintech.origination.public_interface.payment_gate.dto.PaymentRequestDto;
import com.academy.fintech.pg.PaymentRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentGateGrpcMapper {

    PaymentRequest toPaymentRequest(PaymentRequestDto paymentRequestDto);
}
