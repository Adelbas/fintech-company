package com.academy.fintech.origination.core.service.application;

import com.academy.fintech.origination.core.service.application.db.application.ApplicationService;
import com.academy.fintech.origination.core.service.application.db.application.entity.Application;
import com.academy.fintech.origination.core.service.application.db.application.entity.enums.ApplicationStatus;
import com.academy.fintech.origination.core.service.application.db.client.ClientService;
import com.academy.fintech.origination.core.service.application.db.client.entity.Client;
import com.academy.fintech.origination.core.service.export_task.application.ApplicationExportTaskService;
import com.academy.fintech.origination.public_interface.application.dto.ApplicationDto;
import com.academy.fintech.origination.public_interface.application.dto.CancelApplicationDto;
import com.academy.fintech.origination.public_interface.application.exception.ApplicationDuplicateException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ApplicationServiceTest {

    @Mock
    private ApplicationService applicationService;

    @Mock
    private ClientService clientService;

    @Mock
    private ApplicationExportTaskService applicationExportTaskService;

    @InjectMocks
    private ApplicationServiceImpl applicationCreationServiceImpl;

    @Test
    void createApplication_whenClientNotExists() {
        ApplicationDto applicationDto = ApplicationDto
                .builder()
                .firstName("firstname")
                .lastName("lastname")
                .email("email")
                .salary(BigDecimal.valueOf(1000))
                .requestedDisbursementAmount(BigDecimal.valueOf(1000))
                .build();
        Client client = Client.builder()
                .firstName(applicationDto.firstName())
                .lastName(applicationDto.lastName())
                .email(applicationDto.email())
                .salary(applicationDto.salary())
                .build();
        when(clientService.getOrCreate(
                client.getEmail(),
                client.getFirstName(),
                client.getLastName(),
                client.getSalary()
        )).thenReturn(client);

        UUID applicationId = applicationCreationServiceImpl.createApplication(applicationDto);

        verify(applicationService).saveApplication(
                Application.builder()
                        .client(client)
                        .status(ApplicationStatus.NEW)
                        .requestedDisbursementAmount(applicationDto.requestedDisbursementAmount())
                        .build()
        );
        verify(applicationExportTaskService).save(applicationId, ApplicationStatus.NEW);
    }

    @Test
    void createApplication_whenClientExistsAndNoDuplicatesIsFound() {
        ApplicationDto applicationDto = ApplicationDto
                .builder()
                .firstName("firstname")
                .lastName("lastname")
                .email("email")
                .salary(BigDecimal.valueOf(1000))
                .requestedDisbursementAmount(BigDecimal.valueOf(1000))
                .build();
        Client client = Client.builder()
                .clientId(UUID.randomUUID())
                .firstName(applicationDto.firstName())
                .lastName(applicationDto.lastName())
                .email(applicationDto.email())
                .salary(applicationDto.salary())
                .build();
        when(clientService.getOrCreate(
                client.getEmail(),
                client.getFirstName(),
                client.getLastName(),
                client.getSalary()
        )).thenReturn(client);
        when(applicationService.findDuplicateApplication(
                client.getClientId(),
                ApplicationStatus.NEW,
                applicationDto.requestedDisbursementAmount()
        )).thenReturn(Optional.empty());

        UUID applicationId = applicationCreationServiceImpl.createApplication(applicationDto);

        verify(applicationService).saveApplication(
                Application.builder()
                        .client(client)
                        .status(ApplicationStatus.NEW)
                        .requestedDisbursementAmount(applicationDto.requestedDisbursementAmount())
                        .build()
        );
        verify(applicationExportTaskService).save(applicationId, ApplicationStatus.NEW);
    }

    @Test
    void createApplication_whenClientExistsAndDuplicatesIsFound() {
        ApplicationDto applicationDto = ApplicationDto
                .builder()
                .firstName("firstname")
                .lastName("lastname")
                .email("email")
                .salary(BigDecimal.valueOf(1000))
                .requestedDisbursementAmount(BigDecimal.valueOf(1000))
                .build();
        Client client = Client.builder()
                .clientId(UUID.randomUUID())
                .firstName(applicationDto.firstName())
                .lastName(applicationDto.lastName())
                .email(applicationDto.email())
                .salary(applicationDto.salary())
                .build();
        when(clientService.getOrCreate(
                client.getEmail(),
                client.getFirstName(),
                client.getLastName(),
                client.getSalary()
        )).thenReturn(client);
        UUID existApplicationId = UUID.randomUUID();
        when(applicationService.findDuplicateApplication(
                client.getClientId(),
                ApplicationStatus.NEW,
                applicationDto.requestedDisbursementAmount()
        )).thenReturn(Optional.of(Application.builder().applicationId(existApplicationId).build()));

        assertThatExceptionOfType(ApplicationDuplicateException.class)
                .isThrownBy(() -> applicationCreationServiceImpl.createApplication(applicationDto))
                .withMessage(String.valueOf(existApplicationId));

        verify(applicationService, never()).saveApplication(any(Application.class));
    }

    @Test
    void cancelApplication_whenApplicationStatusIsNew() {
        CancelApplicationDto cancelApplicationDto = CancelApplicationDto.builder()
                .applicationId(UUID.randomUUID())
                .build();
        Application application = Application.builder()
                .status(ApplicationStatus.NEW)
                .build();
        when(applicationService.getApplication(cancelApplicationDto.applicationId())).thenReturn(application);

        boolean isCanceled = applicationCreationServiceImpl.cancelApplication(cancelApplicationDto);

        assertThat(isCanceled).isTrue();
        verify(applicationService).removeApplication(application);
    }

    @Test
    void cancelApplication_whenApplicationStatusIsNotNew() {
        CancelApplicationDto cancelApplicationDto = CancelApplicationDto.builder()
                .applicationId(UUID.randomUUID())
                .build();
        Application application = Application.builder()
                .status(ApplicationStatus.SCORING)
                .build();
        when(applicationService.getApplication(cancelApplicationDto.applicationId())).thenReturn(application);

        boolean isCanceled = applicationCreationServiceImpl.cancelApplication(cancelApplicationDto);

        assertThat(isCanceled).isFalse();
        verify(applicationService, never()).removeApplication(application);
    }
}