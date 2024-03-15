package com.academy.fintech.mp.core.service.payment;

import com.academy.fintech.mp.core.service.payment.db.PaymentDbService;
import com.academy.fintech.mp.core.service.payment.db.entity.Payment;
import com.academy.fintech.mp.core.service.payment.db.entity.enums.PaymentStatus;
import com.academy.fintech.mp.core.service.payment.db.entity.enums.PaymentType;
import com.academy.fintech.mp.public_interface.payment.dto.DisbursementRequestDto;
import com.academy.fintech.mp.public_interface.payment.dto.LoanPaymentRequestDto;
import com.academy.fintech.mp.public_interface.payment.dto.PaymentStatusUpdateDto;
import com.academy.fintech.mp.public_interface.payment.dto.StatusCheckResponseDto;
import com.academy.fintech.mp.public_interface.payment.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentDbService paymentDbService;

    @Override
    public UUID disbursePayment(DisbursementRequestDto disbursementRequestDto) {
        return paymentDbService.savePayment(
                Payment.builder()
                        .clientEmail(disbursementRequestDto.clientEmail())
                        .amount(disbursementRequestDto.amount())
                        .paymentType(PaymentType.DISBURSEMENT)
                        .paymentStatus(PaymentStatus.PENDING)
                        .build()
        ).getId();
    }

    @Override
    public UUID loanPayment(LoanPaymentRequestDto loanPaymentRequestDto) {
        return paymentDbService.savePayment(
                Payment.builder()
                        .clientEmail(loanPaymentRequestDto.clientEmail())
                        .amount(loanPaymentRequestDto.amount())
                        .agreementNumber(loanPaymentRequestDto.agreementNumber())
                        .paymentType(PaymentType.LOAN_PAYMENT)
                        .paymentStatus(PaymentStatus.PENDING)
                        .build()
        ).getId();
    }

    @Override
    public StatusCheckResponseDto checkPaymentStatus(UUID id) {
        Payment payment = paymentDbService.getPayment(id);

        return switch (payment.getPaymentStatus()) {
            case PENDING -> StatusCheckResponseDto.builder()
                    .status(payment.getPaymentStatus())
                    .build();
            case FAILED, COMPLETED -> StatusCheckResponseDto.builder()
                    .status(payment.getPaymentStatus())
                    .completionDate(payment.getCompletionDate())
                    .build();
        };
    }

    @Override
    public void updateStatus(PaymentStatusUpdateDto paymentStatusUpdateDto) {
        Payment payment = paymentDbService.getPayment(paymentStatusUpdateDto.paymentId());

        payment.setPaymentStatus(paymentStatusUpdateDto.status());
        payment.setCompletionDate(LocalDateTime.now());
        //TODO: Next hw: handle Loan payment and send LoanPaymentRequest to Payment-gate

        paymentDbService.savePayment(payment);
    }
}
