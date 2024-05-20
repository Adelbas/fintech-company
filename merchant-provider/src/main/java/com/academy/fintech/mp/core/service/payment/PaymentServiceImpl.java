package com.academy.fintech.mp.core.service.payment;

import com.academy.fintech.mp.core.service.payment.client.payment_gate.PaymentGateClientService;
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
import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentDbService paymentDbService;

    private final PaymentGateClientService paymentGateClientService;

    private final Map<PaymentType, Consumer<Payment>> paymentHandler = new EnumMap<>(PaymentType.class);

    {
        paymentHandler.put(PaymentType.LOAN_PAYMENT, this::handleLoanPayment);
    }

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

        paymentDbService.savePayment(payment);
        paymentHandler.get(payment.getPaymentType()).accept(payment);
    }

    private void handleLoanPayment(Payment payment) {
        paymentGateClientService.loanPayment(
                LoanPaymentRequestDto.builder()
                        .clientEmail(payment.getClientEmail())
                        .agreementNumber(payment.getAgreementNumber())
                        .amount(payment.getAmount())
                        .build()
        );
    }
}
