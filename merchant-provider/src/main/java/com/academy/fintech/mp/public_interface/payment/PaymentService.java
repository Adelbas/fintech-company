package com.academy.fintech.mp.public_interface.payment;

import com.academy.fintech.mp.public_interface.payment.dto.DisbursementRequestDto;
import com.academy.fintech.mp.public_interface.payment.dto.LoanPaymentRequestDto;
import com.academy.fintech.mp.public_interface.payment.dto.PaymentStatusUpdateDto;
import com.academy.fintech.mp.public_interface.payment.dto.StatusCheckResponseDto;

import java.util.UUID;

public interface PaymentService {

    UUID disbursePayment(DisbursementRequestDto disbursementRequestDto);
    UUID loanPayment(LoanPaymentRequestDto loanPaymentRequestDto);
    StatusCheckResponseDto checkPaymentStatus(UUID id);
    void updateStatus(PaymentStatusUpdateDto paymentStatusUpdateDto);
}
