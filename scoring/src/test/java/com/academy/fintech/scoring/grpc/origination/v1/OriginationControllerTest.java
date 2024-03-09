package com.academy.fintech.scoring.grpc.origination.v1;

import com.academy.fintech.scoring.ScoringRequest;
import com.academy.fintech.scoring.ScoringResponse;
import com.academy.fintech.scoring.public_interface.ScoringService;
import com.academy.fintech.scoring.public_interface.dto.ScoringRequestDto;
import io.grpc.internal.testing.StreamRecorder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OriginationControllerTest {

    @Mock
    private ScoringService scoringService;

    @Spy
    private OriginationGrpcMapper originationGrpcMapper = new OriginationGrpcMapperImpl();

    @InjectMocks
    private OriginationController originationController;

    @Test
    void scoreApplication() {
        int expectedScore = 2;
        ScoringRequest scoringRequest = mock(ScoringRequest.class);
        StreamRecorder<ScoringResponse> responseObserver = StreamRecorder.create();
        when(scoringService.getScore(any(ScoringRequestDto.class))).thenReturn(expectedScore);

        originationController.scoreApplication(scoringRequest, responseObserver);

        List<ScoringResponse> results = responseObserver.getValues();
        assertThat(results).isNotNull().hasSize(1);
        assertThat(results.get(0).getScore()).isEqualTo(expectedScore);
    }
}