package com.academy.fintech.pg.core.service.payment;

import com.academy.fintech.pg.core.service.payment.db.PaymentDbService;
import com.academy.fintech.pg.core.service.payment.db.entity.Payment;
import com.academy.fintech.pg.public_interface.merchant_provider.dto.StatusCheckRequest;
import com.academy.fintech.pg.public_interface.payment.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentScheduler {

    private final PaymentService paymentService;

    private final PaymentDbService paymentDbService;

    /**
     * Periodically checks database for PENDING payments that are ready to check their status
     */
    @Scheduled(fixedRateString = "${payment.scheduling.interval}", initialDelayString = "${payment.scheduling.initial-delay}")
    public void processNewApplications() {
        log.info("Checking database for pending payments");
        List<Payment> payments = paymentDbService.getPaymentsReadyToCheckStatus();
        if (payments.isEmpty()) {
            return;
        }

        for (Payment payment : payments) {
            log.info("Pulling provider to check payment: {}", payment);

            paymentService.checkPaymentStatus(payment);
        }
    }
}
