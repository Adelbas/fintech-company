package com.academy.fintech.pg.public_interface.payment;

import com.academy.fintech.pg.core.service.payment.db.entity.Payment;
import com.academy.fintech.pg.public_interface.payment.dto.PaymentRequestDto;

public interface PaymentService {
    void sendMoney(PaymentRequestDto paymentRequestDto);

    void checkPaymentStatus(Payment payment);

    void handleLoanPayment(PaymentRequestDto paymentRequestDto);
}
