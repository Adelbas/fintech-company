package com.academy.fintech.pg.rest.merchant_provider.v1;

import com.academy.fintech.pg.public_interface.payment.PaymentService;
import com.academy.fintech.pg.server.api.PaymentGateApi;
import com.academy.fintech.pg.server.model.LoanPaymentRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * Handles loan payments from provider
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class ProviderController implements PaymentGateApi {

    private final PaymentService paymentService;

    private final ProviderRestMapper providerRestMapper;

    @Override
    public ResponseEntity<Void> loanPayment(LoanPaymentRequest loanPaymentRequest) {
        paymentService.handleLoanPayment(providerRestMapper.toPaymentRequestDto(loanPaymentRequest));

        return ResponseEntity.ok().build();
    }
}
