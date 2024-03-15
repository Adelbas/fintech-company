package com.academy.fintech.pg.public_interface.merchant_provider;

import com.academy.fintech.pg.public_interface.merchant_provider.dto.StatusCheckRequest;
import com.academy.fintech.pg.public_interface.merchant_provider.dto.StatusCheckResponse;
import com.academy.fintech.pg.public_interface.merchant_provider.dto.PaymentResponseDto;
import com.academy.fintech.pg.public_interface.payment.dto.PaymentRequestDto;

public interface ProviderService {

    PaymentResponseDto disburseAmount(PaymentRequestDto paymentResponseDto);

    StatusCheckResponse checkPaymentStatus(StatusCheckRequest checkRequest);
}
