package com.academy.fintech.origination.core.service.scoring;

import com.academy.fintech.origination.core.configuration.SchedulerConfiguration;
import com.academy.fintech.origination.core.service.application.db.application.ApplicationService;
import com.academy.fintech.origination.core.service.application.db.application.entity.Application;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

@SpringBootTest(classes = {SchedulerConfiguration.class, ScoringScheduler.class})
public class ScoringSchedulerIntegrationTest {

    @MockBean
    private ApplicationService applicationService;

    @MockBean
    private ScoringService scoringService;

    @SpyBean
    private ScoringScheduler scoringScheduler;

    @Test
    void testSchedulerInitialDelayAndInterval() {
        await()
                .atMost(Duration.ofMillis(1000))
                .untilAsserted(() -> verify(scoringScheduler, atMost(10)).processNewApplications());
    }

    @Test
    void testSchedulerAsyncScoringApplications() {
        List<Application> applications = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            applications.add(mock(Application.class));
        }
        when(applicationService.getNewApplications())
                .thenReturn(applications)
                .thenReturn(new ArrayList<>());

        doAnswer((Answer<Object>) invocation -> {
            new Thread(() -> {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }).start();
            return null;
        }).when(scoringService).scoreApplication(any(Application.class));

        await()
                .atMost(Duration.ofMillis(1000))
                .untilAsserted(() -> {
                    verify(applicationService, times(10)).saveApplication(any(Application.class));
                    verify(scoringService, times(10)).scoreApplication(any(Application.class));
                });
    }
}