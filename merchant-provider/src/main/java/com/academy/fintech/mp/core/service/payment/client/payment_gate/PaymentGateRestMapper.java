package com.academy.fintech.mp.core.service.payment.client.payment_gate;

import com.academy.fintech.mp.public_interface.payment.dto.LoanPaymentRequestDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentGateRestMapper {

    com.academy.fintech.mp.client.model.LoanPaymentRequest toLoanPaymentRequest(LoanPaymentRequestDto loanPaymentRequestDto);
}
