package com.academy.fintech.origination.core.service.scoring;

import com.academy.fintech.origination.core.service.application.db.application.ApplicationService;
import com.academy.fintech.origination.core.service.application.db.application.entity.Application;
import com.academy.fintech.origination.core.service.application.db.application.entity.enums.ApplicationStatus;
import com.academy.fintech.origination.core.service.export_task.application.ApplicationExportTaskService;
import com.academy.fintech.origination.public_interface.scoring.ScoringService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ScoringSchedulerTest {

    @Mock
    private ApplicationService applicationService;

    @Mock
    private ScoringService scoringService;

    @Mock
    private ApplicationExportTaskService applicationExportTaskService;

    @InjectMocks
    private ScoringScheduler scoringScheduler;

    @Test
    void processNewApplications_whenNewApplicationsIsNotFound() {
        when(applicationService.getNewApplications()).thenReturn(new ArrayList<>());

        scoringScheduler.processNewApplications();

        verify(applicationService, only()).getNewApplications();
        verifyNoInteractions(scoringService);
    }

    @Test
    void processNewApplications_whenNewApplicationsIsFound() {
        int applicationsCount = 5;
        List<Application> applications = new ArrayList<>();
        for (int i = 0; i<applicationsCount; i++) {
            applications.add(Application.builder().build());
        }
        when(applicationService.getNewApplications()).thenReturn(applications);

        scoringScheduler.processNewApplications();

        verify(applicationService, times(applicationsCount)).saveApplication(any(Application.class));
        verify(scoringService, times(applicationsCount)).scoreApplication(any(Application.class));
        verify(applicationExportTaskService, times(applicationsCount)).save(any(), eq(ApplicationStatus.SCORING));
    }
}