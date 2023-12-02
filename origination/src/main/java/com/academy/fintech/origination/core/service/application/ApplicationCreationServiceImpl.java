package com.academy.fintech.origination.core.service.application;

import com.academy.fintech.origination.core.service.application.db.application.ApplicationService;
import com.academy.fintech.origination.core.service.application.db.application.entity.Application;
import com.academy.fintech.origination.core.service.application.db.application.entity.enums.ApplicationStatus;
import com.academy.fintech.origination.core.service.application.db.client.ClientService;
import com.academy.fintech.origination.core.service.application.db.client.entity.Client;
import com.academy.fintech.origination.public_interface.application.dto.ApplicationDto;
import com.academy.fintech.origination.public_interface.application.dto.CancelApplicationDto;
import com.academy.fintech.origination.public_interface.application.exception.ApplicationDuplicateException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;
import java.util.UUID;

/**
 * Represents application creation service implementation.
 * Uses {@link ApplicationService} and {@link ClientService} to interact with database.
 * Uses {@link ClientCreationService} to create clients.
 */
@Service
@Validated
@RequiredArgsConstructor
public class ApplicationCreationServiceImpl implements ApplicationCreationService {

    private final ApplicationService applicationService;

    private final ClientService clientService;

    private final ClientCreationService clientCreationService;

    /**
     * Provides application creation.
     * Validates {@link ApplicationDto}.
     * If client not exists in database creates new client.
     * If same application not exists in database creates and saves new application.
     *
     * @param applicationDto applicationDto
     * @return created applicationId
     * @throws ApplicationDuplicateException with applicationId in message if same application exists in database
     */
    @Override
    public UUID createApplication(@Valid ApplicationDto applicationDto) {
        Optional<Client> optionalClient = clientService.findClient(applicationDto.email());
        Client client = optionalClient.orElseGet(
                () -> clientCreationService.createClient(
                        applicationDto.firstName(),
                        applicationDto.lastName(),
                        applicationDto.email(),
                        applicationDto.salary())
        );

        if (optionalClient.isPresent()) {
            Optional<Application> duplicateApplication = applicationService.findDuplicateApplication(
                    client.getClientId(),
                    ApplicationStatus.NEW,
                    applicationDto.requestedDisbursementAmount()
            );

            duplicateApplication.ifPresent((application) -> {
                throw new ApplicationDuplicateException(application.getApplicationId());
            });
        }

        Application application = Application.builder()
                .client(client)
                .requestedDisbursementAmount(applicationDto.requestedDisbursementAmount())
                .status(ApplicationStatus.NEW)
                .build();

        applicationService.saveApplication(application);
        return application.getApplicationId();
    }

    /**
     * Provides application creation canceling.
     * Cancel application and remove it from database if application has status {@link ApplicationStatus#NEW}
     *
     * @param cancelApplicationDto cancelApplicationDto with applicationId
     * @return true if application is canceled, else false
     */
    @Override
    public boolean cancelApplication(CancelApplicationDto cancelApplicationDto) {
        Application application = applicationService.getApplication(cancelApplicationDto.applicationId());

        if (ApplicationStatus.NEW.equals(application.getStatus())) {
            applicationService.removeApplication(application);
            return true;
        }
        return false;
    }
}
