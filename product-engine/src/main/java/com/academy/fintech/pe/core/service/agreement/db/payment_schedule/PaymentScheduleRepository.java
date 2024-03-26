package com.academy.fintech.pe.core.service.agreement.db.payment_schedule;

import com.academy.fintech.pe.core.service.agreement.db.agreement.entity.Agreement;
import com.academy.fintech.pe.core.service.agreement.db.payment_schedule.entity.PaymentSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentScheduleRepository extends JpaRepository<PaymentSchedule, Long> {

    Optional<PaymentSchedule> findFirstByAgreementOrderByVersionDesc(Agreement agreement);
}
