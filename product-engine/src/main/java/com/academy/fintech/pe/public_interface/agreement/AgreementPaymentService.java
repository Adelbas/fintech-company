package com.academy.fintech.pe.public_interface.agreement;

import com.academy.fintech.pe.core.service.agreement.db.agreement.entity.Agreement;
import com.academy.fintech.pe.public_interface.agreement.dto.LoanPaymentRequestDto;

public interface AgreementPaymentService {

    void handleLoanPayment(LoanPaymentRequestDto loanPaymentRequestDto);

    void processOverdue(Agreement agreement);
}
