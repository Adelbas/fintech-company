package com.academy.fintech.origination.core.service.scoring;

import com.academy.fintech.origination.core.service.application.db.application.ApplicationService;
import com.academy.fintech.origination.core.service.application.db.application.entity.Application;
import com.academy.fintech.origination.core.service.application.db.application.entity.enums.ApplicationStatus;
import com.academy.fintech.origination.core.service.email.EmailService;
import com.academy.fintech.origination.core.service.export_task.application.ApplicationExportTaskService;
import com.academy.fintech.origination.core.service.scoring.client.ScoringClientService;
import com.academy.fintech.origination.public_interface.agreement.AgreementService;
import com.academy.fintech.origination.public_interface.agreement.dto.AgreementDto;
import com.academy.fintech.origination.public_interface.application.ApplicationMapper;
import com.academy.fintech.origination.public_interface.scoring.ScoringRequestDto;
import com.academy.fintech.origination.public_interface.scoring.ScoringResponseDto;
import com.academy.fintech.origination.public_interface.scoring.ScoringService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Represents scoring service implementation.
 * Uses {@link ApplicationService} to interact with database.
 * Uses {@link ScoringClientService} to do rpc calls to Scoring microservice
 * Uses {@link ApplicationMapper} to map entity to DTO
 * Uses {@link EmailService} to send emails to clients with scoring results
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ScoringServiceImpl implements ScoringService {

    private static final int DEFAULT_LOAN_TERM = 12;

    private static final String DEFAULT_PRODUCT_CODE = "CL_1.0";

    private static final BigDecimal DEFAULT_INTEREST = new BigDecimal(8);

    private static final BigDecimal DEFAULT_ORIGINATION_AMOUNT = new BigDecimal(10000);

    private final ApplicationService applicationService;

    private final ScoringClientService scoringClientService;

    private final ApplicationMapper applicationMapper;

    private final EmailService emailService;

    private final AgreementService agreementService;

    private final ApplicationExportTaskService applicationExportTaskService;

    /**
     * Provides async application scoring.
     * Gets scoring result from {@link ScoringClientService}.
     * If score is more than zero updates application status to {@link ApplicationStatus#ACTIVE} and sends request to create agreement
     * If score is less or equal to zero updates application status to {@link ApplicationStatus#CLOSED}
     * @param application application to score
     */
    @Async
    @Override
    @Transactional
    public void scoreApplication(Application application) {
        log.info("Scoring application {}", application.getApplicationId());
        ScoringRequestDto scoringRequestDto = ScoringRequestDto.builder()
                .clientId(application.getClient().getClientId())
                .salary(application.getClient().getSalary())
                .disbursementAmount(application.getRequestedDisbursementAmount())
                .interest(DEFAULT_INTEREST)
                .loanTerm(DEFAULT_LOAN_TERM)
                .productCode(DEFAULT_PRODUCT_CODE)
                .originationAmount(DEFAULT_ORIGINATION_AMOUNT)
                .build();

        ScoringResponseDto scoringResponseDto = scoringClientService.scoreApplication(scoringRequestDto);
        int score = scoringResponseDto.score();

        if (score > 0) {
            application.setStatus(ApplicationStatus.ACCEPTED);
        } else {
            application.setStatus(ApplicationStatus.CLOSED);
        }

        log.info("Set status {} for application {}", application.getStatus().name(), application.getApplicationId());
        applicationService.saveApplication(application);
        applicationExportTaskService.save(application.getApplicationId(), application.getStatus());

        sendApplicationStatusEmail(application);

        if (ApplicationStatus.ACCEPTED.equals(application.getStatus())) {
            agreementService.createAgreement(
                    AgreementDto.builder()
                            .clientId(application.getClient().getClientId())
                            .productCode(DEFAULT_PRODUCT_CODE)
                            .interest(DEFAULT_INTEREST)
                            .disbursementAmount(application.getRequestedDisbursementAmount())
                            .originationAmount(DEFAULT_ORIGINATION_AMOUNT)
                            .loanTerm(DEFAULT_LOAN_TERM)
                            .build()
            );
        }
    }

    /**
     * Sends email with {@link EmailService} depending on the {@link ApplicationStatus}
     * @param application scored application
     */
    private void sendApplicationStatusEmail(Application application) {
        if (ApplicationStatus.ACCEPTED.equals(application.getStatus())) {
            emailService.sendApplicationApprovedEmail(applicationMapper.toApplicationEmailDto(application));
        } else if (ApplicationStatus.CLOSED.equals(application.getStatus())) {
            emailService.sendApplicationRejectedEmail(applicationMapper.toApplicationEmailDto(application));
        }
    }
}
