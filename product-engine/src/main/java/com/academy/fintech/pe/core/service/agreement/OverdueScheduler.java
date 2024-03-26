package com.academy.fintech.pe.core.service.agreement;

import com.academy.fintech.pe.core.service.agreement.db.agreement.AgreementService;
import com.academy.fintech.pe.core.service.agreement.db.agreement.entity.Agreement;
import com.academy.fintech.pe.public_interface.agreement.AgreementPaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OverdueScheduler {

    private final AgreementService agreementService;

    private final AgreementPaymentService agreementPaymentService;

    @Scheduled(cron = "${overdue.scheduling.cron-interval}", zone = "${overdue.scheduling.tz}")
    public void processOverdue() {
        log.info("Checking database for overdue check ready agreements");
        List<Agreement> agreements = agreementService.getAgreementsReadyToCheckOverdue();

        for (Agreement agreement : agreements) {
            agreementPaymentService.processOverdue(agreement);
        }
    }
}
