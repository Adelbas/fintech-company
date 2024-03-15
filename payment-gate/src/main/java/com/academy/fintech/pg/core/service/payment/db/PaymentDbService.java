package com.academy.fintech.pg.core.service.payment.db;

import com.academy.fintech.pg.core.service.payment.db.entity.Payment;
import com.academy.fintech.pg.core.service.payment.db.entity.enums.PaymentStatus;
import com.academy.fintech.pg.public_interface.payment.exeption.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentDbService {

    private final PaymentRepository paymentRepository;

    public Payment savePayment(Payment payment) {
        log.info("Saving payment: {}", payment);
        return paymentRepository.save(payment);
    }

    public List<Payment> getPaymentsReadyToCheckStatus() {
        return paymentRepository.findPaymentByPaymentStatusAndPaymentStatusNextCheckDateBefore(
                PaymentStatus.PENDING,
                LocalDateTime.now()
        );
    }

    public Payment getPaymentByExternalId(UUID externalId) {
        return paymentRepository.findByStatusCheckExternalId(externalId)
                .orElseThrow(()->new NotFoundException("Payment not found with externalId " + externalId));
    }
}
