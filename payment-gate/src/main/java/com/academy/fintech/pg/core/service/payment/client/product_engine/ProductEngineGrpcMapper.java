package com.academy.fintech.pg.core.service.payment.client.product_engine;

import com.academy.fintech.pe.LoanPaymentRequest;
import com.academy.fintech.pg.public_interface.payment.dto.PaymentRequestDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductEngineGrpcMapper {

    LoanPaymentRequest toLoanPaymentRequest(PaymentRequestDto paymentRequestDto);
}
