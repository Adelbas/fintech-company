package com.academy.fintech.pe.core.service.agreement;

import com.academy.fintech.pe.core.service.export_task.agreement.AgreementExportStatus;
import com.academy.fintech.pe.core.service.agreement.db.agreement.AgreementService;
import com.academy.fintech.pe.core.service.agreement.db.agreement.entity.Agreement;
import com.academy.fintech.pe.core.service.agreement.db.payment_schedule.PaymentScheduleService;
import com.academy.fintech.pe.core.service.agreement.db.payment_schedule.entity.PaymentSchedule;
import com.academy.fintech.pe.core.service.agreement.db.payment_schedule.entity.PaymentSchedulePayment;
import com.academy.fintech.pe.core.service.agreement.db.payment_schedule.entity.enums.PaymentStatus;
import com.academy.fintech.pe.core.service.export_task.agreement.AgreementExportTaskService;
import com.academy.fintech.pe.public_interface.agreement.AgreementPaymentService;
import com.academy.fintech.pe.public_interface.agreement.dto.LoanPaymentRequestDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AgreementPaymentServiceImpl implements AgreementPaymentService {

    private final AgreementService agreementService;

    private final PaymentScheduleService paymentScheduleService;

    private final AgreementExportTaskService agreementExportTaskService;

    @Override
    public void handleLoanPayment(LoanPaymentRequestDto loanPaymentRequestDto) {
        log.info("Handle loan payment for agreement {} with amount {}", loanPaymentRequestDto.agreementNumber(), loanPaymentRequestDto.amount());
        Agreement agreement = agreementService.getAgreement(loanPaymentRequestDto.agreementNumber());

        BigDecimal amount = loanPaymentRequestDto.amount();

        agreement.setAccountBalance(
                agreement.getAccountBalance().add(amount)
        );

        if (agreement.getOverdueBalance().compareTo(BigDecimal.ZERO) > 0) {
            payOffOverdue(agreement);
        }

        agreementService.saveAgreement(agreement);
    }

    /**
     * Increases overdue balance by the period payment amount.
     * Paying overdue by {@link #payOffOverdue(Agreement)}
     * If overdue balance equals zero sets payment's status to {@link PaymentStatus#PAID PAID}
     * Else if overdue balance is more than zero sets payment's status to {@link PaymentStatus#OVERDUE OVERDUE}
     *
     * @param agreement
     */
    @Async
    @Override
    @Transactional
    public void processOverdue(Agreement agreement) {
        log.info("Agreement {} is processing for overdue", agreement.getAgreementNumber());
        PaymentSchedule paymentSchedule = paymentScheduleService.getActualPaymentScheduleForAgreement(agreement);
        PaymentSchedulePayment currentPayment = getCurrentPayment(agreement, paymentSchedule);

        BigDecimal oldOverdueBalance = agreement.getOverdueBalance();
        agreement.setOverdueBalance(
                agreement.getOverdueBalance().add(currentPayment.getPeriodPayment())
        );

        payOffOverdue(agreement);

        if (agreement.getOverdueBalance().equals(BigDecimal.ZERO)) {
            currentPayment.setStatus(PaymentStatus.PAID);

            //If status was OVERDUE, now send ACTIVE
            if (oldOverdueBalance.compareTo(BigDecimal.ZERO) < 0) {
                agreementExportTaskService.save(agreement.getAgreementNumber(), AgreementExportStatus.ACTIVE);
            }
        } else {
            currentPayment.setStatus(PaymentStatus.OVERDUE);

            //Send status OVERDUE
            agreementExportTaskService.save(agreement.getAgreementNumber(), AgreementExportStatus.OVERDUE);
        }

        agreement.setNextPaymentDate(getNextPeriodPaymentDate(paymentSchedule, currentPayment));

        log.info("Agreement {} processing ended.\nResult: Payment status is {}", agreement.getAgreementNumber(), currentPayment.getStatus());
        agreementService.saveAgreement(agreement);
    }

    /**
     * Pay off overdue by subtracting the overdue amount from account balance
     *
     * @param agreement
     */
    private void payOffOverdue(Agreement agreement) {
        BigDecimal diff = agreement.getAccountBalance().subtract(agreement.getOverdueBalance());

        if (diff.compareTo(BigDecimal.ZERO) < 0) {
            agreement.setAccountBalance(BigDecimal.ZERO);
            agreement.setOverdueBalance(diff.abs());
        } else {
            agreement.setAccountBalance(diff.abs());
            agreement.setOverdueBalance(BigDecimal.ZERO);
        }
    }

    /**
     * Gets current payment from latest payment schedule
     *
     * @param agreement
     * @return
     */
    private PaymentSchedulePayment getCurrentPayment(Agreement agreement, PaymentSchedule paymentSchedule) {
        return paymentSchedule.getPayments()
                .stream()
                .filter(p->p.getPaymentDate().equals(agreement.getNextPaymentDate()))
                .filter(p->p.getStatus().equals(PaymentStatus.FUTURE))
                .findFirst()
                .orElseThrow(()->new RuntimeException("Next payment not found"));
    }

    /**
     * Gets next payment date by payment period
     *
     * @param paymentSchedule
     * @param currentPayment
     * @return next payment date if next payment is exists, else current payment date
     */
    private LocalDateTime getNextPeriodPaymentDate(PaymentSchedule paymentSchedule, PaymentSchedulePayment currentPayment) {
        int nextPeriod = currentPayment.getPeriodNumber() + 1;

        return paymentSchedule.getPayments()
                .stream()
                .filter(p->p.getPeriodNumber().equals(nextPeriod))
                .findFirst()
                .orElse(currentPayment)
                .getPaymentDate();
    }
}
