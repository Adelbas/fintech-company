package com.academy.fintech.pg.core.service.merchant_provider.client;

import com.academy.fintech.pg.core.service.merchant_provider.client.rest.MerchantProviderRestClient;
import com.academy.fintech.pg.public_interface.merchant_provider.dto.PaymentResponseDto;
import com.academy.fintech.pg.public_interface.merchant_provider.dto.StatusCheckRequest;
import com.academy.fintech.pg.public_interface.payment.dto.PaymentRequestDto;
import com.academy.fintech.pg.client.model.PaymentDisbursementRequest;
import com.academy.fintech.pg.client.model.PaymentResponse;
import com.academy.fintech.pg.public_interface.merchant_provider.dto.StatusCheckResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MerchantProviderClientService {

    private final MerchantProviderRestClient merchantProviderRestClient;

    private final MerchantProviderRestMapper merchantProviderRestMapper;

    public PaymentResponseDto disburseAmount(PaymentRequestDto paymentRequestDto) {
        log.info("Sending disbursement payment request to Provider: {}", paymentRequestDto);
        PaymentDisbursementRequest paymentDisbursementRequest = merchantProviderRestMapper.toPaymentDisbursementRequest(paymentRequestDto);

        PaymentResponse paymentResponse = merchantProviderRestClient.disburse(paymentDisbursementRequest);
        log.info("Got payment response from Provider: {}", paymentResponse);

        return merchantProviderRestMapper.toPaymentResponseDto(paymentResponse);
    }

    public StatusCheckResponse checkResponse (StatusCheckRequest statusCheckRequest) {
        log.info("Sending check status request to Provider: {}", statusCheckRequest);
        com.academy.fintech.pg.client.model.StatusCheckResponse statusCheckResponse = merchantProviderRestClient.getPaymentInfo(statusCheckRequest.statusCheckId());

        log.info("Got status response: {}", statusCheckResponse);

        return merchantProviderRestMapper.toStatusCheckResponse(statusCheckResponse);
    }
}
