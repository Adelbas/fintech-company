package com.academy.fintech.origination.core.service.agreement;

import com.academy.fintech.origination.core.service.agreement.client.product_enginie.ProductEngineClientService;
import com.academy.fintech.origination.core.service.agreement.db.agreement.AgreementDbService;
import com.academy.fintech.origination.core.service.agreement.db.agreement.entity.Agreement;
import com.academy.fintech.origination.core.service.application.db.client.ClientService;
import com.academy.fintech.origination.core.service.application.db.client.entity.Client;
import com.academy.fintech.origination.public_interface.agreement.AgreementService;
import com.academy.fintech.origination.public_interface.agreement.dto.AgreementActivationDto;
import com.academy.fintech.origination.public_interface.agreement.dto.AgreementDto;
import com.academy.fintech.origination.public_interface.payment_gate.PaymentGateService;
import com.academy.fintech.origination.public_interface.payment_gate.dto.DisbursementStatusUpdateDto;
import com.academy.fintech.origination.public_interface.payment_gate.dto.PaymentRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AgreementServiceImpl implements AgreementService {

    private final ProductEngineClientService productEngineClientService;

    private final AgreementDbService agreementDbService;

    private final ClientService clientService;

    private final PaymentGateService paymentGateService;

    /**
     * Sends agreement creation request to product-engine and saves agreement number to db.
     * Sends disbursement by payment-gate
     * @param agreementDto
     */
    @Override
    public void createAgreement(AgreementDto agreementDto) {
        UUID agreementNumber = productEngineClientService.createAgreement(agreementDto);
        Client client = clientService.getClient(agreementDto.clientId());

        agreementDbService.saveAgreement(
                Agreement.builder()
                        .agreementNumber(agreementNumber)
                        .client(client)
                        .isDisbursementCompleted(false)
                        .build()
        );

        paymentGateService.sendMoney(
                PaymentRequestDto.builder()
                        .clientEmail(client.getEmail())
                        .agreementNumber(agreementNumber)
                        .amount(agreementDto.disbursementAmount())
                        .build()
        );
    }

    /**
     * Updates disbursement status and sends activation request to product-engine
     * @param disbursementStatusUpdateDto
     */
    @Override
    public void updateDisbursementStatus(DisbursementStatusUpdateDto disbursementStatusUpdateDto) {
        if (disbursementStatusUpdateDto.isDisbursementCompletedSuccessfully()) {
            Agreement agreement = agreementDbService.getAgreement(disbursementStatusUpdateDto.agreementNumber());
            log.info("Updating agreement status to completed: {}", agreement.getAgreementNumber());

            agreement.setDisbursementCompleted(true);
            agreementDbService.saveAgreement(agreement);

            productEngineClientService.activateAgreement(AgreementActivationDto.builder()
                    .agreementNumber(disbursementStatusUpdateDto.agreementNumber())
                    .disbursementDate(disbursementStatusUpdateDto.disbursementDate())
                    .build());
        }
    }
}
