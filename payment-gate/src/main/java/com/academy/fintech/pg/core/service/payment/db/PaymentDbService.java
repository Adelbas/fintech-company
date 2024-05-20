package com.academy.fintech.pg.core.service.payment.db;

import com.academy.fintech.pg.core.service.payment.db.entity.Payment;
import com.academy.fintech.pg.core.service.payment.db.entity.enums.PaymentStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentDbService {

    private static final int PAYMENTS_RESULT_LIMIT = 100;

    private final PaymentRepository paymentRepository;

    public Payment savePayment(Payment payment) {
        log.info("Saving payment: {}", payment);
        return paymentRepository.save(payment);
    }

    public List<Payment> getPaymentsReadyToCheckStatus() {
        return paymentRepository.findPaymentByPaymentStatusAndPaymentStatusNextCheckDateBefore(
                PaymentStatus.PENDING,
                LocalDateTime.now(),
                PageRequest.of(0,PAYMENTS_RESULT_LIMIT)
        );
    }
}
