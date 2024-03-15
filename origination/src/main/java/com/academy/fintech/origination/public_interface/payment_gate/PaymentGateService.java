package com.academy.fintech.origination.public_interface.payment_gate;

import com.academy.fintech.origination.public_interface.payment_gate.dto.PaymentRequestDto;

public interface PaymentGateService {
    void sendMoney(PaymentRequestDto paymentRequestDto);
}
