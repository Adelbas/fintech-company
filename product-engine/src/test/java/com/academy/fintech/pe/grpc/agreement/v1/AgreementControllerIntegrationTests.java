package com.academy.fintech.pe.grpc.agreement.v1;

import com.academy.fintech.pe.*;
import com.academy.fintech.pe.core.service.agreement.db.agreement.AgreementService;
import com.academy.fintech.pe.core.service.agreement.db.agreement.entity.Agreement;
import com.academy.fintech.pe.core.service.agreement.db.agreement.entity.enums.AgreementStatus;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Testcontainers
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@DirtiesContext
public class AgreementControllerIntegrationTests {

    private static final String HOSTNAME = "localhost";

    private static final int GRPC_PORT = 9090;

    @Container
    private static final PostgreSQLContainer<?> postgresContainer =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:14.1-alpine"));

    @DynamicPropertySource
    private static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @Autowired
    private AgreementService agreementService;

    private static AgreementServiceGrpc.AgreementServiceBlockingStub blockingStub;
    private static ManagedChannel channel;

    @BeforeAll
    static void setUp() {
        channel = ManagedChannelBuilder.forAddress(HOSTNAME, GRPC_PORT)
                .usePlaintext()
                .build();
        blockingStub = AgreementServiceGrpc.newBlockingStub(channel);
    }

    @AfterAll
    public static void tearDown() {
        channel.shutdown();
    }

    @Test
    @Transactional
    void createAgreement_whenAgreementRequestIsValid_shouldSaveAgreementInDb() {
        Agreement expectedAgreement = Agreement.builder()
                .clientId(UUID.randomUUID())
                .originationAmount(BigDecimal.valueOf(10000))
                .principalAmount(BigDecimal.valueOf(500000))
                .interest(BigDecimal.valueOf(8))
                .loanTerm(12)
                .status(AgreementStatus.NEW)
                .paymentSchedules(new ArrayList<>())
                .build();
        AgreementRequest agreementRequest = AgreementRequest.newBuilder()
                .setProductCode("CL_1.0")
                .setClientId(String.valueOf(expectedAgreement.getClientId()))
                .setInterest(String.valueOf(expectedAgreement.getInterest()))
                .setLoanTerm(expectedAgreement.getLoanTerm())
                .setOriginationAmount(String.valueOf(expectedAgreement.getOriginationAmount()))
                .setDisbursementAmount(String.valueOf(expectedAgreement.getPrincipalAmount()
                        .subtract(expectedAgreement.getOriginationAmount())))
                .build();

        AgreementResponse agreementResponse = blockingStub.createAgreement(agreementRequest);
        UUID createdAgreementNumber = UUID.fromString(agreementResponse.getAgreementNumber());
        Agreement actualAgreement = agreementService.getAgreement(createdAgreementNumber);

        assertThat(actualAgreement).usingRecursiveComparison()
                .ignoringFields("agreementNumber", "product")
                .isEqualTo(expectedAgreement);
        assertThat(actualAgreement.getProduct().getCode()).isEqualTo(agreementRequest.getProductCode());
    }

    @ParameterizedTest(name = "{index} - {1} exception")
    @MethodSource("provideArgumentsForCreateAgreement_whenAgreementRequestIsInvalid_shouldReturnErrorTest")
    void createAgreement_whenAgreementRequestIsInvalid_shouldReturnError(AgreementRequest agreementRequest, Status.Code code) {
        StatusRuntimeException statusRuntimeException = assertThrows(StatusRuntimeException.class,
                () -> blockingStub.createAgreement(agreementRequest));

        assertThat(statusRuntimeException.getStatus().getCode()).isEqualTo(code);
    }

    @Test
    void activateAgreement_whenAgreementIsNotActivated_shouldReturnPaymentSchedule() {
        AgreementRequest agreementRequest = AgreementRequest.newBuilder()
                .setProductCode("CL_1.0")
                .setClientId(String.valueOf(UUID.randomUUID()))
                .setInterest(String.valueOf(8))
                .setLoanTerm(12)
                .setOriginationAmount(String.valueOf(10000))
                .setDisbursementAmount(String.valueOf(490000))
                .build();
        String agreementNumber = blockingStub.createAgreement(agreementRequest).getAgreementNumber();
        LocalDateTime disbursementDate = LocalDateTime.parse("2023-11-19T05:55:39.6831515");
        AgreementActivationRequest agreementActivationRequest = AgreementActivationRequest.newBuilder()
                .setAgreementNumber(agreementNumber)
                .setDisbursementDate(String.valueOf(disbursementDate))
                .build();

        String payment = "43494.21";
        String status = "FUTURE";
        List<PaymentSchedulePaymentResponse> expectedPayments = List.of(
                PaymentSchedulePaymentResponse.newBuilder()
                        .setPeriod(1)
                        .setPayment(payment)
                        .setInterest("3333.33")
                        .setPrincipal("40160.88")
                        .setBalance("459839.12")
                        .setPaymentDate("2023-12-19T05:55:39.6831515")
                        .setStatus(status)
                        .build(),
                PaymentSchedulePaymentResponse.newBuilder()
                        .setPeriod(2)
                        .setPayment(payment)
                        .setInterest("3065.59")
                        .setPrincipal("40428.62")
                        .setBalance("419410.50")
                        .setPaymentDate("2024-01-19T05:55:39.6831515")
                        .setStatus(status)
                        .build(),
                PaymentSchedulePaymentResponse.newBuilder()
                        .setPeriod(3)
                        .setPayment(payment)
                        .setInterest("2796.07")
                        .setPrincipal("40698.14")
                        .setBalance("378712.36")
                        .setPaymentDate("2024-02-19T05:55:39.6831515")
                        .setStatus(status)
                        .build(),
                PaymentSchedulePaymentResponse.newBuilder()
                        .setPeriod(4)
                        .setPayment(payment)
                        .setInterest("2524.75")
                        .setPrincipal("40969.46")
                        .setBalance("337742.90")
                        .setPaymentDate("2024-03-19T05:55:39.6831515")
                        .setStatus(status)
                        .build(),
                PaymentSchedulePaymentResponse.newBuilder()
                        .setPeriod(5)
                        .setPayment(payment)
                        .setInterest("2251.62")
                        .setPrincipal("41242.59")
                        .setBalance("296500.31")
                        .setPaymentDate("2024-04-19T05:55:39.6831515")
                        .setStatus(status)
                        .build(),
                PaymentSchedulePaymentResponse.newBuilder()
                        .setPeriod(6)
                        .setPayment(payment)
                        .setInterest("1976.67")
                        .setPrincipal("41517.54")
                        .setBalance("254982.77")
                        .setPaymentDate("2024-05-19T05:55:39.6831515")
                        .setStatus(status)
                        .build(),
                PaymentSchedulePaymentResponse.newBuilder()
                        .setPeriod(7)
                        .setPayment(payment)
                        .setInterest("1699.88")
                        .setPrincipal("41794.33")
                        .setBalance("213188.44")
                        .setPaymentDate("2024-06-19T05:55:39.6831515")
                        .setStatus(status)
                        .build(),
                PaymentSchedulePaymentResponse.newBuilder()
                        .setPeriod(8)
                        .setPayment(payment)
                        .setInterest("1421.25")
                        .setPrincipal("42072.96")
                        .setBalance("171115.48")
                        .setPaymentDate("2024-07-19T05:55:39.6831515")
                        .setStatus(status)
                        .build(),
                PaymentSchedulePaymentResponse.newBuilder()
                        .setPeriod(9)
                        .setPayment(payment)
                        .setInterest("1140.77")
                        .setPrincipal("42353.44")
                        .setBalance("128762.04")
                        .setPaymentDate("2024-08-19T05:55:39.6831515")
                        .setStatus(status)
                        .build(),
                PaymentSchedulePaymentResponse.newBuilder()
                        .setPeriod(10)
                        .setPayment(payment)
                        .setInterest("858.41")
                        .setPrincipal("42635.80")
                        .setBalance("86126.24")
                        .setPaymentDate("2024-09-19T05:55:39.6831515")
                        .setStatus(status)
                        .build(),
                PaymentSchedulePaymentResponse.newBuilder()
                        .setPeriod(11)
                        .setPayment(payment)
                        .setInterest("574.17")
                        .setPrincipal("42920.04")
                        .setBalance("43206.20")
                        .setPaymentDate("2024-10-19T05:55:39.6831515")
                        .setStatus(status)
                        .build(),
                PaymentSchedulePaymentResponse.newBuilder()
                        .setPeriod(12)
                        .setPayment("43494.24")
                        .setInterest("288.04")
                        .setPrincipal("43206.20")
                        .setBalance("0.00")
                        .setPaymentDate("2024-11-19T05:55:39.6831515")
                        .setStatus(status)
                        .build()
        );
        PaymentScheduleResponse expectedPaymentScheduleResponse = PaymentScheduleResponse.newBuilder().
                addAllPayments(expectedPayments)
                .setVersion(1)
                .build();

        PaymentScheduleResponse actualPaymentScheduleResponse = blockingStub.activateAgreement(agreementActivationRequest);

        assertThat(actualPaymentScheduleResponse).isEqualTo(expectedPaymentScheduleResponse);
    }

    @Test
    void activateAgreement_whenAgreementIsAlreadyActivated_shouldReturnError() {
        AgreementRequest agreementRequest = AgreementRequest.newBuilder()
                .setProductCode("CL_1.0")
                .setClientId(String.valueOf(UUID.randomUUID()))
                .setInterest(String.valueOf(8))
                .setLoanTerm(12)
                .setOriginationAmount(String.valueOf(10000))
                .setDisbursementAmount(String.valueOf(490000))
                .build();
        String agreementNumber = blockingStub.createAgreement(agreementRequest).getAgreementNumber();
        LocalDateTime disbursementDate = LocalDateTime.now();
        AgreementActivationRequest agreementActivationRequest = AgreementActivationRequest.newBuilder()
                .setAgreementNumber(agreementNumber)
                .setDisbursementDate(String.valueOf(disbursementDate))
                .build();
        blockingStub.activateAgreement(agreementActivationRequest);

        StatusRuntimeException statusRuntimeException = assertThrows(StatusRuntimeException.class,
                () -> blockingStub.activateAgreement(agreementActivationRequest));

        assertThat(statusRuntimeException.getStatus().getCode()).isEqualTo(Status.Code.INVALID_ARGUMENT);
    }

    @Test
    void activateAgreement_whenAgreementNotExists_shouldReturnError() {
        LocalDateTime disbursementDate = LocalDateTime.now();
        AgreementActivationRequest agreementActivationRequest = AgreementActivationRequest.newBuilder()
                .setAgreementNumber(String.valueOf(UUID.randomUUID()))
                .setDisbursementDate(String.valueOf(disbursementDate))
                .build();

        StatusRuntimeException statusRuntimeException = assertThrows(StatusRuntimeException.class,
                () -> blockingStub.activateAgreement(agreementActivationRequest));

        assertThat(statusRuntimeException.getStatus().getCode()).isEqualTo(Status.Code.NOT_FOUND);
    }

    private static Stream<Arguments> provideArgumentsForCreateAgreement_whenAgreementRequestIsInvalid_shouldReturnErrorTest() {
        return Stream.of(
                Arguments.of(
                        AgreementRequest.newBuilder()
                                .setProductCode("CL_2.0")
                                .setClientId(UUID.randomUUID().toString())
                                .setInterest(String.valueOf(8))
                                .setLoanTerm(12)
                                .setOriginationAmount(String.valueOf(10000))
                                .setDisbursementAmount(String.valueOf(490000))
                                .build(),
                        Status.Code.NOT_FOUND
                ),
                Arguments.of(
                        AgreementRequest.newBuilder()
                                .setProductCode("CL_1.0")
                                .setClientId(UUID.randomUUID().toString())
                                .setInterest(String.valueOf(-2))
                                .setLoanTerm(12)
                                .setOriginationAmount(String.valueOf(10000))
                                .setDisbursementAmount(String.valueOf(490000))
                                .build(),
                        Status.Code.INVALID_ARGUMENT
                ),
                Arguments.of(
                        AgreementRequest.newBuilder()
                                .setProductCode("CL_1.0")
                                .setClientId(UUID.randomUUID().toString())
                                .setInterest(String.valueOf(8))
                                .setLoanTerm(90)
                                .setOriginationAmount(String.valueOf(10000))
                                .setDisbursementAmount(String.valueOf(490000))
                                .build(),
                        Status.Code.INVALID_ARGUMENT
                ),
                Arguments.of(
                        AgreementRequest.newBuilder()
                                .setProductCode("CL_1.0")
                                .setClientId(UUID.randomUUID().toString())
                                .setInterest(String.valueOf(8))
                                .setLoanTerm(12)
                                .setOriginationAmount(String.valueOf(0))
                                .setDisbursementAmount(String.valueOf(490000))
                                .build(),
                        Status.Code.INVALID_ARGUMENT
                )
        );
    }
}
