package com.academy.fintech.mp.core.service.payment.db;

import com.academy.fintech.mp.core.service.payment.db.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {
}
