package com.academy.fintech.pe.core.service.agreement;

import com.academy.fintech.pe.core.service.agreement.db.agreement.AgreementService;
import com.academy.fintech.pe.core.service.agreement.db.agreement.entity.Agreement;
import com.academy.fintech.pe.public_interface.agreement.AgreementPaymentService;
import com.academy.fintech.pe.public_interface.agreement.dto.LoanPaymentRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AgreementPaymentServiceImpl implements AgreementPaymentService {

    private final AgreementService agreementService;

    @Override
    public void handleLoanPayment(LoanPaymentRequestDto loanPaymentRequestDto) {
        Agreement agreement = agreementService.getAgreement(loanPaymentRequestDto.agreementNumber());

        BigDecimal amount = loanPaymentRequestDto.amount();

        if (agreement.getOverdueBalance().compareTo(BigDecimal.ZERO) < 0) {
            amount = payOffOverdue(agreement, amount);
        }

        agreement.setAccountBalance(
                agreement.getAccountBalance().add(amount)
        );

        agreementService.saveAgreement(agreement);
    }

    /**
     * Pay off overdue by subtracting the amount from overdue balance
     *
     * @param agreement
     * @param amount
     * @return remaining amount of money to add to account balance
     */
    private BigDecimal payOffOverdue(Agreement agreement, BigDecimal amount) {
        BigDecimal diff = agreement.getOverdueBalance().subtract(amount);

        if (diff.compareTo(BigDecimal.ZERO) < 0) {
            agreement.setOverdueBalance(BigDecimal.ZERO);
            return diff.abs();
        }

        agreement.setOverdueBalance(diff);
        return BigDecimal.ZERO;
    }
}
