package com.academy.fintech.origination.core.service.scoring;

import com.academy.fintech.origination.core.service.application.db.application.ApplicationService;
import com.academy.fintech.origination.core.service.application.db.application.entity.Application;
import com.academy.fintech.origination.core.service.application.db.application.entity.enums.ApplicationStatus;
import com.academy.fintech.origination.core.service.application.db.client.entity.Client;
import com.academy.fintech.origination.core.service.email.EmailService;
import com.academy.fintech.origination.core.service.export_task.application.ApplicationExportTaskService;
import com.academy.fintech.origination.core.service.scoring.client.ScoringClientService;
import com.academy.fintech.origination.public_interface.agreement.AgreementService;
import com.academy.fintech.origination.public_interface.agreement.dto.AgreementDto;
import com.academy.fintech.origination.public_interface.application.ApplicationMapper;
import com.academy.fintech.origination.public_interface.application.ApplicationMapperImpl;
import com.academy.fintech.origination.public_interface.application.dto.ApplicationEmailDto;
import com.academy.fintech.origination.public_interface.scoring.ScoringRequestDto;
import com.academy.fintech.origination.public_interface.scoring.ScoringResponseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ScoringServiceTest {

    @Mock
    private ApplicationService applicationService;

    @Mock
    private ScoringClientService scoringClientService;

    @Mock
    private EmailService emailService;

    @Mock
    private AgreementService agreementService;

    @Mock
    private ApplicationExportTaskService applicationExportTaskService;

    @Spy
    private ApplicationMapper applicationMapper = new ApplicationMapperImpl();

    @InjectMocks
    private ScoringServiceImpl scoringService;

    @Test
    void scoreApplication_whenApplicationIsAccepted() {
        Application application = Application.builder()
                .client(Client.builder()
                        .clientId(UUID.randomUUID())
                        .salary(BigDecimal.valueOf(90000))
                        .build())
                .requestedDisbursementAmount(BigDecimal.valueOf(500000))
                .status(ApplicationStatus.NEW)
                .build();
        ScoringResponseDto scoringResponseDto = ScoringResponseDto.builder()
                .score(2)
                .build();
        when(scoringClientService.scoreApplication(any(ScoringRequestDto.class))).thenReturn(scoringResponseDto);

        scoringService.scoreApplication(application);

        assertThat(application.getStatus()).isEqualTo(ApplicationStatus.ACCEPTED);
        verify(applicationService).saveApplication(application);
        verify(emailService).sendApplicationApprovedEmail(any(ApplicationEmailDto.class));
        verify(agreementService).createAgreement(any(AgreementDto.class));
        verify(applicationExportTaskService).save(any(), eq(ApplicationStatus.ACCEPTED));
    }

    @Test
    void scoreApplication_whenApplicationIsRejected() {
        Application application = Application.builder()
                .client(Client.builder()
                        .clientId(UUID.randomUUID())
                        .salary(BigDecimal.valueOf(90000))
                        .build())
                .requestedDisbursementAmount(BigDecimal.valueOf(500000))
                .status(ApplicationStatus.NEW)
                .build();
        ScoringResponseDto scoringResponseDto = ScoringResponseDto.builder()
                .score(0)
                .build();
        when(scoringClientService.scoreApplication(any(ScoringRequestDto.class))).thenReturn(scoringResponseDto);

        scoringService.scoreApplication(application);

        assertThat(application.getStatus()).isEqualTo(ApplicationStatus.CLOSED);
        verify(applicationService).saveApplication(application);
        verify(emailService).sendApplicationRejectedEmail(any(ApplicationEmailDto.class));
        verify(applicationExportTaskService).save(any(), eq(ApplicationStatus.CLOSED));
    }
}