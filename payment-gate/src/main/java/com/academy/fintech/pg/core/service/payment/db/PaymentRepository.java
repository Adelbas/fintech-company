package com.academy.fintech.pg.core.service.payment.db;

import com.academy.fintech.pg.core.service.payment.db.entity.Payment;
import com.academy.fintech.pg.core.service.payment.db.entity.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    List<Payment> findPaymentByPaymentStatusAndPaymentStatusNextCheckDateBefore(PaymentStatus paymentStatus, LocalDateTime paymentStatusNextCheckDate);

    Optional<Payment> findByStatusCheckExternalId(UUID id);
}
