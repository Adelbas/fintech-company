package com.academy.fintech.origination.core.service.application.db.application;

import com.academy.fintech.origination.core.service.application.db.application.entity.Application;
import com.academy.fintech.origination.core.service.application.db.application.entity.enums.ApplicationStatus;
import com.academy.fintech.origination.core.service.application.db.client.entity.Client;
import com.academy.fintech.origination.public_interface.application.exception.NotFoundException;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ApplicationServiceTest {

    @Mock
    private ApplicationRepository applicationRepository;

    @InjectMocks
    private ApplicationService applicationService;

    @Test
    void saveApplication() {
        final Application application = mock(Application.class);
        when(applicationRepository.save(application)).thenReturn(application);

        applicationService.saveApplication(application);

        verify(applicationRepository, only()).save(application);
    }

    @Test
    void getApplication_whenApplicationExists() {
        final UUID applicationId = UUID.randomUUID();
        final Application expectedApplication = mock(Application.class, withSettings().defaultAnswer(CALLS_REAL_METHODS));
        expectedApplication.setApplicationId(applicationId);
        when(applicationRepository.findById(applicationId)).thenReturn(Optional.of(expectedApplication));

        Application actualApplication = applicationService.getApplication(applicationId);

        assertThat(actualApplication).isEqualTo(expectedApplication);
        verify(applicationRepository, only()).findById(applicationId);
    }

    @Test
    void getApplication_whenApplicationNotExists() {
        final UUID applicationId = UUID.randomUUID();
        when(applicationRepository.findById(applicationId)).thenReturn(Optional.empty());

        assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> applicationService.getApplication(applicationId));
        verify(applicationRepository, only()).findById(applicationId);
    }

    @Test
    void findDuplicateApplication_whenApplicationExists() {
        final UUID clientId = UUID.randomUUID();
        final Client client = Client.builder()
                .clientId(clientId)
                .build();
        final ApplicationStatus status = ApplicationStatus.NEW;
        final BigDecimal requiredDisbursementAmount = BigDecimal.valueOf(30000);
        final Application expectedApplication = Application.builder()
                .client(client)
                .status(status)
                .requestedDisbursementAmount(requiredDisbursementAmount)
                .build();
        when(applicationRepository.findApplicationByClientClientIdAndStatusAndRequestedDisbursementAmount(
                clientId,
                status,
                requiredDisbursementAmount)
        ).thenReturn(Optional.of(expectedApplication));

        Optional<Application> actualApplication = applicationService.findDuplicateApplication(clientId, status, requiredDisbursementAmount);

        assertThat(actualApplication)
                .isNotEmpty()
                .hasValue(expectedApplication);
        verify(applicationRepository, only()).findApplicationByClientClientIdAndStatusAndRequestedDisbursementAmount(clientId, status, requiredDisbursementAmount);
    }

    @Test
    void findDuplicateApplication_whenApplicationNotExists() {
        final UUID clientId = UUID.randomUUID();
        final ApplicationStatus status = ApplicationStatus.NEW;
        final BigDecimal requiredDisbursementAmount = BigDecimal.valueOf(30000);
        when(applicationRepository.findApplicationByClientClientIdAndStatusAndRequestedDisbursementAmount(
                clientId,
                status,
                requiredDisbursementAmount)
        ).thenReturn(Optional.empty());

        Optional<Application> actualApplication = applicationService.findDuplicateApplication(clientId, status, requiredDisbursementAmount);

        assertThat(actualApplication).isEmpty();
        verify(applicationRepository, only()).findApplicationByClientClientIdAndStatusAndRequestedDisbursementAmount(clientId, status, requiredDisbursementAmount);
    }

    @Test
    void removeApplication() {
        Application application = mock(Application.class);

        applicationService.removeApplication(application);

        verify(applicationRepository, only()).delete(application);
    }
}