package com.academy.fintech.mp.core.service.payment.client.payment_gate;

import com.academy.fintech.mp.core.service.payment.client.payment_gate.rest.PaymentGateRestClient;
import com.academy.fintech.mp.public_interface.payment.dto.LoanPaymentRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentGateClientService {

    private final PaymentGateRestClient paymentGateRestClient;

    private final PaymentGateRestMapper paymentGateRestMapper;

    public void loanPayment(LoanPaymentRequestDto loanPaymentRequestDto) {
        log.info("Sending loan payment to Payment-gate: {}", loanPaymentRequestDto);
        paymentGateRestClient.loanPayment(paymentGateRestMapper.toLoanPaymentRequest(loanPaymentRequestDto));
    }
}
