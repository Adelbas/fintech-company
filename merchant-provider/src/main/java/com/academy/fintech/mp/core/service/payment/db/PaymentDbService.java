package com.academy.fintech.mp.core.service.payment.db;

import com.academy.fintech.mp.core.service.payment.db.entity.Payment;
import com.academy.fintech.mp.public_interface.payment.exeption.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

    public Payment getPayment(UUID id) {
        return paymentRepository.findById(id).orElseThrow(()-> new NotFoundException("Payment not found"));
    }
}
