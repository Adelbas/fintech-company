package com.academy.fintech.pe.public_interface.agreement;

import com.academy.fintech.pe.public_interface.agreement.dto.AgreementActivationDto;
import com.academy.fintech.pe.public_interface.agreement.dto.AgreementDto;
import com.academy.fintech.pe.public_interface.agreement.dto.PaymentScheduleDto;

import java.util.UUID;

/**
 * Interface that provides agreement creation and its activation
 */
public interface AgreementCreationService {
    UUID createAgreement(AgreementDto agreementDto);

    PaymentScheduleDto activateAgreement(AgreementActivationDto agreementActivationDto);
}
