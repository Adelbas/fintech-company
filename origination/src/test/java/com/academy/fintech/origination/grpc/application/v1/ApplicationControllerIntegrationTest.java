package com.academy.fintech.origination.grpc.application.v1;

import com.academy.fintech.application.ApplicationRequest;
import com.academy.fintech.application.ApplicationResponse;
import com.academy.fintech.application.ApplicationServiceGrpc;
import com.academy.fintech.application.CancelApplicationRequest;
import com.academy.fintech.application.CancelApplicationResponse;
import com.academy.fintech.origination.core.service.application.db.application.ApplicationRepository;
import com.academy.fintech.origination.core.service.application.db.application.entity.Application;
import com.academy.fintech.origination.core.service.application.db.application.entity.enums.ApplicationStatus;
import com.academy.fintech.origination.core.service.application.db.client.entity.Client;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

@Testcontainers
@SpringBootTest(classes = com.academy.fintech.origination.Application.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ApplicationControllerIntegrationTest {

    private static final String HOSTNAME = "localhost";

    private static final int GRPC_PORT = 9094;

    @Container
    private static final PostgreSQLContainer<?> postgresContainer =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:14.1-alpine"));

    @DynamicPropertySource
    private static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
        registry.add("grpc.port", () -> GRPC_PORT);
    }

    @Autowired
    private ApplicationRepository applicationRepository;

    private static ApplicationServiceGrpc.ApplicationServiceBlockingStub blockingStub;
    private static ManagedChannel channel;

    @BeforeAll
    static void setUp() {
        channel = ManagedChannelBuilder.forAddress(HOSTNAME, GRPC_PORT)
                .usePlaintext()
                .build();
        blockingStub = ApplicationServiceGrpc.newBlockingStub(channel);
    }

    @AfterAll
    public static void tearDown() {
        channel.shutdown();
    }

    @Test
    void createApplication_whenClientNotExists_shouldSaveClientAndApplicationInDb() {
        Client client = Client.builder()
                .firstName("firstname")
                .lastName("lastname")
                .email("test@mail.ru")
                .salary(BigDecimal.valueOf(300000))
                .build();
        ApplicationRequest applicationRequest = ApplicationRequest.newBuilder()
                .setEmail(client.getEmail())
                .setFirstName(client.getFirstName())
                .setLastName(client.getLastName())
                .setDisbursementAmount(100000)
                .setSalary(client.getSalary().intValue())
                .build();
        Application expectedApplication = Application.builder()
                .client(client)
                .status(ApplicationStatus.NEW)
                .requestedDisbursementAmount(BigDecimal.valueOf(applicationRequest.getDisbursementAmount()))
                .build();

        ApplicationResponse applicationResponse = blockingStub.create(applicationRequest);
        UUID createdApplicationId = UUID.fromString(applicationResponse.getApplicationId());

        Optional<Application> actualApplication = applicationRepository.findById(createdApplicationId);
        assertThat(actualApplication).isPresent();
        assertThat(actualApplication.get())
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringFields("applicationId", "client.clientId")
                .isEqualTo(expectedApplication);
    }

    @Test
    void createDuplicateApplication_whenClientExists_shouldReturnApplicationIdInTrailers() {
        ApplicationRequest applicationRequest = ApplicationRequest.newBuilder()
                .setEmail("test2@mail.ru")
                .setFirstName("firstname")
                .setLastName("lastname")
                .setDisbursementAmount(500000)
                .setSalary(30000)
                .build();

        ApplicationResponse applicationResponse = blockingStub.create(applicationRequest);
        String applicationId = applicationResponse.getApplicationId();

        StatusRuntimeException statusRuntimeException = assertThrows(StatusRuntimeException.class,
                () -> blockingStub.create(applicationRequest));

        assertThat(statusRuntimeException.getStatus().getCode()).isEqualTo(Status.Code.ALREADY_EXISTS);
        assertThat(statusRuntimeException.getTrailers()).isNotNull();
        assertThat(statusRuntimeException.getTrailers().get(Metadata.Key.of("application_id", Metadata.ASCII_STRING_MARSHALLER)))
                .isEqualTo(applicationId);
    }

    @Test
    void cancelApplication_whenApplicationIsExistsAndHasStatusNew_shouldReturnRemoveApplicationFromDb() {
        ApplicationRequest applicationRequest = ApplicationRequest.newBuilder()
                .setEmail("test3@mail.ru")
                .setFirstName("firstname")
                .setLastName("lastname")
                .setDisbursementAmount(500000)
                .setSalary(30000)
                .build();
        ApplicationResponse applicationResponse = blockingStub.create(applicationRequest);
        String applicationId = applicationResponse.getApplicationId();
        CancelApplicationRequest cancelApplicationRequest = CancelApplicationRequest.newBuilder()
                .setApplicationId(applicationResponse.getApplicationId())
                .build();

        CancelApplicationResponse cancelApplicationResponse = blockingStub.cancelApplication(cancelApplicationRequest);

        assertThat(cancelApplicationResponse.getIsCanceled()).isTrue();
        Optional<Application> applicationInDb = applicationRepository.findById(UUID.fromString(applicationId));
        assertThat(applicationInDb).isEmpty();
    }

    @Test
    void cancelApplication_whenApplicationIsExistsAndHasNotStatusNew_shouldReturnFalse() {
        ApplicationRequest applicationRequest = ApplicationRequest.newBuilder()
                .setEmail("test4@mail.ru")
                .setFirstName("firstname")
                .setLastName("lastname")
                .setDisbursementAmount(500000)
                .setSalary(30000)
                .build();
        ApplicationResponse applicationResponse = blockingStub.create(applicationRequest);
        String applicationId = applicationResponse.getApplicationId();
        Application application = applicationRepository.findById(UUID.fromString(applicationId)).get();
        application.setStatus(ApplicationStatus.SCORING);
        applicationRepository.save(application);
        CancelApplicationRequest cancelApplicationRequest = CancelApplicationRequest.newBuilder()
                .setApplicationId(applicationResponse.getApplicationId())
                .build();

        CancelApplicationResponse cancelApplicationResponse = blockingStub.cancelApplication(cancelApplicationRequest);

        assertThat(cancelApplicationResponse.getIsCanceled()).isFalse();
        Optional<Application> applicationInDb = applicationRepository.findById(UUID.fromString(applicationId));
        assertThat(applicationInDb).isPresent();
    }

    @Test
    void cancelApplication_whenApplicationIsNotExists_shouldReturnException() {
        CancelApplicationRequest cancelApplicationRequest = CancelApplicationRequest.newBuilder()
                .setApplicationId(UUID.randomUUID().toString())
                .build();

        StatusRuntimeException statusRuntimeException = assertThrows(StatusRuntimeException.class,
                () -> blockingStub.cancelApplication(cancelApplicationRequest));

        assertThat(statusRuntimeException.getStatus().getCode()).isEqualTo(Status.Code.NOT_FOUND);
    }

    @ParameterizedTest(name = "{index} - {2}")
    @MethodSource("provideArgumentsForCreateApplication_whenInvalidRequest_shouldReturnExceptionAndDescriptionInTrailersTest")
    void createApplication_whenInvalidRequest_shouldReturnExceptionAndDescriptionInTrailers(ApplicationRequest applicationRequest, String fieldName, String errorMessage) {
        StatusRuntimeException statusRuntimeException = assertThrows(StatusRuntimeException.class,
                () -> blockingStub.create(applicationRequest));

        assertThat(statusRuntimeException.getStatus().getCode()).isEqualTo(Status.Code.INVALID_ARGUMENT);
        assertThat(statusRuntimeException.getTrailers()).isNotNull();
        assertThat(statusRuntimeException.getTrailers().get(Metadata.Key.of(fieldName, Metadata.ASCII_STRING_MARSHALLER)))
                .isEqualTo(errorMessage);
    }

    private static Stream<Arguments> provideArgumentsForCreateApplication_whenInvalidRequest_shouldReturnExceptionAndDescriptionInTrailersTest() {
        return Stream.of(
                Arguments.of(
                        ApplicationRequest.newBuilder()
                                .setEmail("testmail.ru")
                                .setFirstName("firstname")
                                .setLastName("lastname")
                                .setDisbursementAmount(500000)
                                .setSalary(30000)
                                .build(),
                        "email",
                        "Invalid email"
                ),
                Arguments.of(
                        ApplicationRequest.newBuilder()
                                .setEmail("test@mail.ru")
                                .setFirstName("")
                                .setLastName("lastname")
                                .setDisbursementAmount(500000)
                                .setSalary(30000)
                                .build(),
                        "firstname",
                        "Empty first name"
                ),
                Arguments.of(
                        ApplicationRequest.newBuilder()
                                .setEmail("test@mail.ru")
                                .setFirstName("firstname")
                                .setLastName("")
                                .setDisbursementAmount(500000)
                                .setSalary(30000)
                                .build(),
                        "lastname",
                        "Empty last name"
                ),
                Arguments.of(
                        ApplicationRequest.newBuilder()
                                .setEmail("test@mail.ru")
                                .setFirstName("firstname")
                                .setLastName("lastname")
                                .setDisbursementAmount(-500000)
                                .setSalary(30000)
                                .build(),
                        "requesteddisbursementamount",
                        "Invalid requested disbursement amount"
                ),
                Arguments.of(
                        ApplicationRequest.newBuilder()
                                .setEmail("test@mail.ru")
                                .setFirstName("firstname")
                                .setLastName("lastname")
                                .setDisbursementAmount(500000)
                                .setSalary(0)
                                .build(),
                        "salary",
                        "Invalid salary"
                )
        );
    }
}
