package com.academy.fintech.pg.rest.merchant_provider.v1;

import com.academy.fintech.pg.public_interface.payment.dto.PaymentRequestDto;
import com.academy.fintech.pg.server.model.LoanPaymentRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProviderRestMapper {
    PaymentRequestDto toPaymentRequestDto(LoanPaymentRequest loanPaymentRequest);
}
