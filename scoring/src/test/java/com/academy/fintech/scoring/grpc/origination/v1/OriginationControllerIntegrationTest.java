package com.academy.fintech.scoring.grpc.origination.v1;

import com.academy.fintech.scoring.AgreementResponse;
import com.academy.fintech.scoring.PaymentSchedulePaymentResponse;
import com.academy.fintech.scoring.PaymentScheduleResponse;
import com.academy.fintech.scoring.ScoringDataRequest;
import com.academy.fintech.scoring.ScoringDataResponse;
import com.academy.fintech.scoring.ScoringRequest;
import com.academy.fintech.scoring.ScoringResponse;
import com.academy.fintech.scoring.ScoringServiceGrpc;
import com.academy.fintech.scoring.core.product_engine.client.grpc.ProductEngineGrpcClient;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OriginationControllerIntegrationTest {

    @Value("${grpc.host}")
    private String host;

    @Value("${grpc.port}")
    private int port;

    private static ScoringServiceGrpc.ScoringServiceBlockingStub blockingStub;
    private static ManagedChannel channel;

    @MockBean
    private ProductEngineGrpcClient productEngineGrpcClient;

    @BeforeAll
    void setUp() {
        channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        blockingStub = ScoringServiceGrpc.newBlockingStub(channel);
    }

    @AfterAll
    public void tearDown() {
        channel.shutdown();
    }

    @Test
    void getScore_whenSalaryIsValidAndNoAgreements() {
        int expectedScore = 2;
        ScoringRequest scoringRequest = ScoringRequest.newBuilder()
                .setClientId(UUID.randomUUID().toString())
                .setSalary(95000)
                .setProductCode("CL_1.0")
                .setLoanTerm(12)
                .setInterest("10")
                .setDisbursementAmount("350000")
                .setOriginationAmount("10000")
                .build();
        ScoringDataResponse scoringDataResponse = ScoringDataResponse.newBuilder()
                        .setPeriodPayment("30000")
                        .addAllActiveAgreements(new ArrayList<>())
                        .build();
        when(productEngineGrpcClient.getScoringData(any(ScoringDataRequest.class))).thenReturn(scoringDataResponse);

        ScoringResponse scoringResponse = blockingStub.scoreApplication(scoringRequest);

        assertThat(scoringResponse.getScore()).isEqualTo(expectedScore);
    }

    @Test
    void getScore_whenSalaryInValidAndNoAgreements() {
        int expectedScore = 1;
        ScoringRequest scoringRequest = ScoringRequest.newBuilder()
                .setClientId(UUID.randomUUID().toString())
                .setSalary(80000)
                .setProductCode("CL_1.0")
                .setLoanTerm(12)
                .setInterest("10")
                .setDisbursementAmount("350000")
                .setOriginationAmount("10000")
                .build();
        ScoringDataResponse scoringDataResponse = ScoringDataResponse.newBuilder()
                .setPeriodPayment("30000")
                .addAllActiveAgreements(new ArrayList<>())
                .build();
        when(productEngineGrpcClient.getScoringData(any(ScoringDataRequest.class))).thenReturn(scoringDataResponse);

        ScoringResponse scoringResponse = blockingStub.scoreApplication(scoringRequest);

        assertThat(scoringResponse.getScore()).isEqualTo(expectedScore);
    }

    @Test
    void getScore_whenSalaryIsValidAndNoOverdue() {
        int expectedScore = 2;
        ScoringRequest scoringRequest = ScoringRequest.newBuilder()
                .setClientId(UUID.randomUUID().toString())
                .setSalary(91000)
                .setProductCode("CL_1.0")
                .setLoanTerm(12)
                .setInterest("10")
                .setDisbursementAmount("350000")
                .setOriginationAmount("10000")
                .build();
        List<PaymentSchedulePaymentResponse> payments = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            payments.add(
                    generatePaymentSchedulePaymentResponse("FUTURE", LocalDateTime.now().plusMonths(1))
            );
        }
        PaymentScheduleResponse paymentScheduleResponse = PaymentScheduleResponse.newBuilder()
                .setVersion(1)
                .addAllPayments(payments)
                .build();
        ScoringDataResponse scoringDataResponse = ScoringDataResponse.newBuilder()
                .setPeriodPayment("30000")
                .addAllActiveAgreements(List.of(
                       generateAgreementResponse(List.of(paymentScheduleResponse))))
                .build();
        when(productEngineGrpcClient.getScoringData(any(ScoringDataRequest.class))).thenReturn(scoringDataResponse);

        ScoringResponse scoringResponse = blockingStub.scoreApplication(scoringRequest);

        assertThat(scoringResponse.getScore()).isEqualTo(expectedScore);
    }

    @Test
    void getScore_whenSalaryInvalidAndOverdueOf9Days() {
        int expectedScore = -1;
        ScoringRequest scoringRequest = ScoringRequest.newBuilder()
                .setClientId(UUID.randomUUID().toString())
                .setSalary(40000)
                .setProductCode("CL_1.0")
                .setLoanTerm(12)
                .setInterest("10")
                .setDisbursementAmount("490000")
                .setOriginationAmount("10000")
                .build();
        List<PaymentSchedulePaymentResponse> payments = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            payments.add(
                    generatePaymentSchedulePaymentResponse("FUTURE", LocalDateTime.now().plusMonths(9))
            );
        }
        payments.add(
                generatePaymentSchedulePaymentResponse("OVERDUE", LocalDateTime.now().minusDays(9))
        );
        PaymentScheduleResponse paymentScheduleResponse = PaymentScheduleResponse.newBuilder()
                .setVersion(1)
                .addAllPayments(payments)
                .build();
        ScoringDataResponse scoringDataResponse = ScoringDataResponse.newBuilder()
                .setPeriodPayment("30000")
                .addAllActiveAgreements(List.of(
                        generateAgreementResponse(List.of(paymentScheduleResponse))))
                .build();
        when(productEngineGrpcClient.getScoringData(any(ScoringDataRequest.class))).thenReturn(scoringDataResponse);

        ScoringResponse scoringResponse = blockingStub.scoreApplication(scoringRequest);

        assertThat(scoringResponse.getScore()).isEqualTo(expectedScore);
    }

    private PaymentSchedulePaymentResponse generatePaymentSchedulePaymentResponse(String status, LocalDateTime paymentDate) {
        return PaymentSchedulePaymentResponse.newBuilder()
                .setStatus(status)
                .setInterest("10000")
                .setPayment("10000")
                .setPrincipal("10000")
                .setPeriod(3)
                .setPaymentDate(paymentDate.toString())
                .build();
    }

    private AgreementResponse generateAgreementResponse(List<PaymentScheduleResponse> paymentScheduleResponses) {
        return AgreementResponse.newBuilder()
                .setInterest("10000")
                .setAgreementNumber(UUID.randomUUID().toString())
                .setOriginationAmount("10000")
                .setPrincipalAmount("10000")
                .setDisbursementDate(LocalDateTime.now().toString())
                .setNextPaymentDate(LocalDateTime.now().toString())
                .addAllPaymentSchedules(paymentScheduleResponses)
                .build();
    }
}