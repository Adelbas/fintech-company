package com.academy.fintech.pe.core.service.agreement.db.payment_schedule;

import com.academy.fintech.pe.core.service.agreement.db.agreement.entity.Agreement;
import com.academy.fintech.pe.core.service.agreement.db.payment_schedule.entity.PaymentSchedule;
import com.academy.fintech.pe.public_interface.agreement.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentScheduleService {

    private final PaymentScheduleRepository paymentScheduleRepository;

    public PaymentSchedule getActualPaymentScheduleForAgreement(Agreement agreement) {
        return paymentScheduleRepository.findFirstByAgreementOrderByVersionDesc(agreement)
                .orElseThrow(()->new NotFoundException("Payment Schedule not found for agreement " + agreement.getAgreementNumber()));
    }
}
