package com.academy.fintech.pe.grpc.agreement.v1;

import com.academy.fintech.pe.AgreementActivationRequest;
import com.academy.fintech.pe.AgreementRequest;
import com.academy.fintech.pe.AgreementResponse;
import com.academy.fintech.pe.PaymentScheduleResponse;
import com.academy.fintech.pe.public_interface.agreement.AgreementCreationService;
import com.academy.fintech.pe.public_interface.agreement.dto.AgreementActivationDto;
import com.academy.fintech.pe.public_interface.agreement.dto.AgreementDto;
import com.academy.fintech.pe.public_interface.agreement.dto.PaymentScheduleDto;
import io.grpc.internal.testing.StreamRecorder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AgreementControllerTest {

    @Mock
    private AgreementCreationService agreementCreationService;

    @Spy
    private AgreementGrpcMapper agreementGrpcMapper = new AgreementGrpcMapperImpl();

    @InjectMocks
    private AgreementController agreementController;

    @Test
    void createAgreement() {
        AgreementRequest agreementRequest = mock(AgreementRequest.class);
        UUID expectedAgreementNumber = UUID.randomUUID();
        StreamRecorder<AgreementResponse> responseObserver = StreamRecorder.create();
        when(agreementCreationService.createAgreement(any(AgreementDto.class))).thenReturn(expectedAgreementNumber);

        agreementController.createAgreement(agreementRequest, responseObserver);

        List<AgreementResponse> results = responseObserver.getValues();
        assertThat(results).isNotNull().hasSize(1);
        assertThat(results.get(0).getAgreementNumber()).isEqualTo(expectedAgreementNumber.toString());
    }

    @Test
    void activateAgreement() {
        AgreementActivationRequest agreementActivationRequest = mock(AgreementActivationRequest.class);
        StreamRecorder<PaymentScheduleResponse> responseObserver = StreamRecorder.create();
        PaymentScheduleDto paymentScheduleDto = PaymentScheduleDto.builder()
                .version(1)
                .payments(new ArrayList<>())
                .build();
        when(agreementCreationService.activateAgreement(any(AgreementActivationDto.class))).thenReturn(paymentScheduleDto);

        agreementController.activateAgreement(agreementActivationRequest, responseObserver);

        List<PaymentScheduleResponse> results = responseObserver.getValues();
        assertThat(results).isNotNull().hasSize(1);
        assertThat(results.get(0).getVersion()).isEqualTo(paymentScheduleDto.version());
        assertThat(results.get(0).getPaymentsList()).isEqualTo(paymentScheduleDto.payments());
    }
}