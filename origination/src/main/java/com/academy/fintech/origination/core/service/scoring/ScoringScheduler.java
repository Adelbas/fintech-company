package com.academy.fintech.origination.core.service.scoring;

import com.academy.fintech.origination.core.service.application.db.application.ApplicationService;
import com.academy.fintech.origination.core.service.application.db.application.entity.Application;
import com.academy.fintech.origination.core.service.application.db.application.entity.enums.ApplicationStatus;
import com.academy.fintech.origination.public_interface.scoring.ScoringService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Represents scoring scheduler.
 * Searches for applications with status {@link ApplicationStatus#NEW} in database.
 * Update found applications status to {@link ApplicationStatus#SCORING} and calls async {@link ScoringService#scoreApplication}
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ScoringScheduler {

    private final ApplicationService applicationService;

    private final ScoringService scoringService;

    @Scheduled(fixedRateString = "${scoring.scheduling.interval}", initialDelayString = "${scoring.scheduling.initial-delay}")
    public void processNewApplications() {
        log.info("Checking database for new applications");
        List<Application> applications = applicationService.getNewApplications();
        if (applications.isEmpty()) {
            return;
        }

        for (Application application : applications) {
            log.info("Set status {} for application {}", ApplicationStatus.SCORING.name(), application.getApplicationId());
            application.setStatus(ApplicationStatus.SCORING);
            applicationService.saveApplication(application);

            scoringService.scoreApplication(application);
        }
    }
}
