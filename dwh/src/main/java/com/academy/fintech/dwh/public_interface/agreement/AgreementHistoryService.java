package com.academy.fintech.dwh.public_interface.agreement;

import com.academy.fintech.dwh.public_interface.agreement.dto.AgreementStatusUpdateDto;

public interface AgreementHistoryService {

    void handleAgreementStatusUpdate(AgreementStatusUpdateDto agreementStatusUpdateDto);
}
