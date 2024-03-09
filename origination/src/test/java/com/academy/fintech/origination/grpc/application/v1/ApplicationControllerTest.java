package com.academy.fintech.origination.grpc.application.v1;

import com.academy.fintech.application.ApplicationRequest;
import com.academy.fintech.application.ApplicationResponse;
import com.academy.fintech.application.CancelApplicationRequest;
import com.academy.fintech.application.CancelApplicationResponse;
import com.academy.fintech.origination.public_interface.application.ApplicationService;
import com.academy.fintech.origination.public_interface.application.dto.ApplicationDto;
import com.academy.fintech.origination.public_interface.application.dto.CancelApplicationDto;
import io.grpc.internal.testing.StreamRecorder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ApplicationControllerTest {

    @Mock
    private ApplicationService applicationService;

    @Spy
    private ApplicationGrpcMapper applicationGrpcMapper = new ApplicationGrpcMapperImpl();

    @InjectMocks
    private ApplicationController applicationController;

    @Test
    void create() {
        ApplicationRequest applicationRequest = mock(ApplicationRequest.class);
        UUID expectedApplicationId = UUID.randomUUID();
        StreamRecorder<ApplicationResponse> responseObserver = StreamRecorder.create();
        when(applicationService.createApplication(any(ApplicationDto.class))).thenReturn(expectedApplicationId);

        applicationController.create(applicationRequest, responseObserver);

        List<ApplicationResponse> results = responseObserver.getValues();
        assertThat(results).isNotNull().hasSize(1);
        assertThat(results.get(0).getApplicationId()).isEqualTo(expectedApplicationId.toString());
    }

    @Test
    void cancelApplication_whenApplicationIsCanceled() {
        CancelApplicationRequest cancelApplicationRequest = mock(CancelApplicationRequest.class);
        StreamRecorder<CancelApplicationResponse> responseObserver = StreamRecorder.create();
        when(applicationService.cancelApplication(any(CancelApplicationDto.class))).thenReturn(true);

        applicationController.cancelApplication(cancelApplicationRequest, responseObserver);

        List<CancelApplicationResponse> results = responseObserver.getValues();
        assertThat(results).isNotNull().hasSize(1);
        assertThat(results.get(0).getIsCanceled()).isTrue();
    }

    @Test
    void cancelApplication_whenApplicationIsNotCanceled() {
        CancelApplicationRequest cancelApplicationRequest = mock(CancelApplicationRequest.class);
        StreamRecorder<CancelApplicationResponse> responseObserver = StreamRecorder.create();
        when(applicationService.cancelApplication(any(CancelApplicationDto.class))).thenReturn(false);

        applicationController.cancelApplication(cancelApplicationRequest, responseObserver);

        List<CancelApplicationResponse> results = responseObserver.getValues();
        assertThat(results).isNotNull().hasSize(1);
        assertThat(results.get(0).getIsCanceled()).isFalse();
    }
}