package com.academy.fintech.pg.core.service.merchant_provider;

import com.academy.fintech.pg.core.service.merchant_provider.client.MerchantProviderClientService;
import com.academy.fintech.pg.public_interface.merchant_provider.ProviderService;
import com.academy.fintech.pg.public_interface.merchant_provider.dto.StatusCheckRequest;
import com.academy.fintech.pg.public_interface.merchant_provider.dto.StatusCheckResponse;
import com.academy.fintech.pg.public_interface.merchant_provider.dto.PaymentResponseDto;
import com.academy.fintech.pg.public_interface.payment.dto.PaymentRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MerchantProviderServiceImpl implements ProviderService {

    private final MerchantProviderClientService merchantProviderClientService;

    @Override
    public PaymentResponseDto disburseAmount(PaymentRequestDto paymentRequestDto) {
        return merchantProviderClientService.disburseAmount(paymentRequestDto);
    }

    @Override
    public StatusCheckResponse checkPaymentStatus(StatusCheckRequest statusCheckRequest) {
        return merchantProviderClientService.checkResponse(statusCheckRequest);
    }
}
