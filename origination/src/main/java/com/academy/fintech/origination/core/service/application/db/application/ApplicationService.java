package com.academy.fintech.origination.core.service.application.db.application;

import com.academy.fintech.origination.core.service.application.db.application.entity.Application;
import com.academy.fintech.origination.core.service.application.db.application.entity.enums.ApplicationStatus;
import com.academy.fintech.origination.public_interface.application.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;

    public void saveApplication(Application application) {
        applicationRepository.save(application);
    }

    public Application getApplication(UUID applicationId) {
        return applicationRepository.findById(applicationId).orElseThrow(() -> new NotFoundException("Application not found"));
    }

    /**
     * Searches application's duplicate by given parameters in database
     *
     * @param clientId clientId
     * @param applicationStatus application status
     * @param requestedDisbursementAmount requested disbursement amount
     * @return Optional of application
     */
    public Optional<Application> findDuplicateApplication(UUID clientId,
                                                          ApplicationStatus applicationStatus,
                                                          BigDecimal requestedDisbursementAmount) {
        return applicationRepository.findApplicationByClientClientIdAndStatusAndRequestedDisbursementAmount(
                clientId,
                applicationStatus,
                requestedDisbursementAmount
        );
    }

    public void removeApplication(Application application) {
        applicationRepository.delete(application);
    }

    public List<Application> getNewApplications() {
        return applicationRepository.findByStatus(ApplicationStatus.NEW)
                .orElse(new ArrayList<>());
    }
}
