package com.academy.fintech.origination.core.service.application;

import com.academy.fintech.origination.core.service.application.db.application.entity.Application;
import com.academy.fintech.origination.core.service.application.db.application.entity.enums.ApplicationStatus;
import com.academy.fintech.origination.core.service.application.db.client.ClientService;
import com.academy.fintech.origination.core.service.application.db.client.entity.Client;
import com.academy.fintech.origination.public_interface.application.ApplicationService;
import com.academy.fintech.origination.public_interface.application.dto.ApplicationDto;
import com.academy.fintech.origination.public_interface.application.dto.CancelApplicationDto;
import com.academy.fintech.origination.public_interface.application.exception.ApplicationDuplicateException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;
import java.util.UUID;

/**
 * Represents application creation service implementation.
 * Uses {@link com.academy.fintech.origination.core.service.application.db.application.ApplicationService} and {@link ClientService} to interact with database.
 */
@Service
@Validated
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final com.academy.fintech.origination.core.service.application.db.application.ApplicationService applicationService;

    private final ClientService clientService;

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
    @Transactional
    public UUID createApplication(@Valid ApplicationDto applicationDto) {
        Client client = clientService.getOrCreate(
                applicationDto.email(),
                applicationDto.firstName(),
                applicationDto.lastName(),
                applicationDto.salary()
        );

        Optional<Application> duplicateApplication = applicationService.findDuplicateApplication(
                client.getClientId(),
                ApplicationStatus.NEW,
                applicationDto.requestedDisbursementAmount()
        );
        duplicateApplication.ifPresent((application) -> {
            throw new ApplicationDuplicateException(application.getApplicationId());
        });

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
