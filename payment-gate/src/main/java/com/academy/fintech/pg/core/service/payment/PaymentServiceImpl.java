package com.academy.fintech.pg.core.service.payment;

import com.academy.fintech.pg.core.calculation.payment.PaymentUtils;
import com.academy.fintech.pg.core.service.payment.client.origination.OriginationClientService;
import com.academy.fintech.pg.core.service.payment.db.PaymentDbService;
import com.academy.fintech.pg.core.service.payment.db.entity.Payment;
import com.academy.fintech.pg.core.service.payment.db.entity.enums.PaymentStatus;
import com.academy.fintech.pg.core.service.payment.db.entity.enums.PaymentType;
import com.academy.fintech.pg.public_interface.merchant_provider.ProviderService;
import com.academy.fintech.pg.public_interface.merchant_provider.dto.PaymentResponseDto;
import com.academy.fintech.pg.public_interface.merchant_provider.dto.StatusCheckRequest;
import com.academy.fintech.pg.public_interface.merchant_provider.dto.StatusCheckResponse;
import com.academy.fintech.pg.public_interface.payment.PaymentService;
import com.academy.fintech.pg.public_interface.payment.dto.PaymentRequestDto;
import com.academy.fintech.pg.public_interface.payment.dto.UpdatePaymentStatusDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final ProviderService providerService;

    private final PaymentDbService paymentDbService;

    private final OriginationClientService originationClientService;

    /**
     * Sends request to Provider and gets UUID from response to check payment status in future.
     * Saves payment to db.
     * @param paymentRequestDto
     */
    @Override
    public void sendMoney(PaymentRequestDto paymentRequestDto) {
        PaymentResponseDto paymentResponseDto = providerService.disburseAmount(paymentRequestDto);

        paymentDbService.savePayment(
                Payment.builder()
                        .clientEmail(paymentRequestDto.clientEmail())
                        .amount(paymentRequestDto.amount())
                        .agreementNumber(paymentRequestDto.agreementNumber())
                        .statusCheckExternalId(paymentResponseDto.statusCheckId())
                        .paymentType(PaymentType.DISBURSEMENT)
                        .paymentStatus(PaymentStatus.PENDING)
                        .paymentDate(LocalDateTime.now())
                        .paymentStatusNextCheckDate(PaymentUtils.calculateNextCheckingDate())
                        .countOfStatusChecks(0)
                        .build()
        );
    }

    /**
     * Async sends requests to Provider to check payment status and increase payment's statusCheckCounter
     * If status is still PENDING, calculates next checking date and saves it.
     * If payment is completed, updates payment status in db and sends request to Origination
     * @param payment
     */
    @Async
    @Override
    @Transactional
    public void checkPaymentStatus(Payment payment) {
        StatusCheckResponse statusCheckResponse = providerService.checkPaymentStatus(
                StatusCheckRequest.builder()
                        .statusCheckId(payment.getStatusCheckExternalId())
                        .build()
        );

        payment.setCountOfStatusChecks(payment.getCountOfStatusChecks() + 1);

        if (PaymentStatus.PENDING.equals(statusCheckResponse.status())) {
            this.updateStatusCheckingDate(payment);
            return;
        }

        this.onPaymentCompleted(
                payment,
                statusCheckResponse.status(),
                statusCheckResponse.completionDate()
        );
    }

    private void updateStatusCheckingDate(Payment payment) {
        payment.setPaymentStatusNextCheckDate(
                PaymentUtils.calculateNextCheckingDate(
                        payment.getPaymentStatusNextCheckDate(),
                        payment.getCountOfStatusChecks()
                )
        );

        paymentDbService.savePayment(payment);
    }

    private void onPaymentCompleted(Payment payment, PaymentStatus status, LocalDateTime completionDate) {
        payment.setPaymentStatus(status);
        payment.setPaymentStatusUpdateDate(completionDate);
        paymentDbService.savePayment(payment);

        originationClientService.updatePaymentStatus(UpdatePaymentStatusDto.builder()
                .agreementNumber(payment.getAgreementNumber())
                .status(payment.getPaymentStatus())
                .completionDate(payment.getPaymentStatusUpdateDate())
                .build()
        );
    }
}
