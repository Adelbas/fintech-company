package com.academy.fintech.origination.core.service.payment_gate;

import com.academy.fintech.origination.core.service.payment_gate.client.PaymentGateClientService;
import com.academy.fintech.origination.public_interface.agreement.AgreementService;
import com.academy.fintech.origination.public_interface.agreement.dto.AgreementActivationDto;
import com.academy.fintech.origination.public_interface.payment_gate.PaymentGateService;
import com.academy.fintech.origination.public_interface.payment_gate.dto.DisbursementStatusUpdateDto;
import com.academy.fintech.origination.public_interface.payment_gate.dto.PaymentRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentGateServiceImpl implements PaymentGateService {

    private final PaymentGateClientService paymentGateClientService;

    @Override
    public void sendMoney(PaymentRequestDto paymentRequestDto) {
        log.info("Send disbursement request to Payment-gate: {}", paymentRequestDto);
        paymentGateClientService.sendMoney(paymentRequestDto);
    }
}
