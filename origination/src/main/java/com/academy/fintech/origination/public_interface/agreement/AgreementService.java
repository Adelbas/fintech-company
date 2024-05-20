package com.academy.fintech.origination.public_interface.agreement;

import com.academy.fintech.origination.public_interface.agreement.dto.AgreementDto;
import com.academy.fintech.origination.public_interface.payment_gate.dto.DisbursementStatusUpdateDto;

/**
 * Interface that provides agreement creation and its activation
 */
public interface AgreementService {
    void createAgreement(AgreementDto agreementDto);


    void updateDisbursementStatus(DisbursementStatusUpdateDto disbursementStatusUpdateDto);
}
