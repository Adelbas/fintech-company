package com.academy.fintech.pe.core.service.agreement;

import com.academy.fintech.pe.core.service.agreement.db.agreement.AgreementService;
import com.academy.fintech.pe.core.service.agreement.db.agreement.entity.Agreement;
import com.academy.fintech.pe.core.service.agreement.db.agreement.entity.enums.AgreementStatus;
import com.academy.fintech.pe.core.service.agreement.db.product.ProductService;
import com.academy.fintech.pe.core.service.agreement.db.product.entity.Product;
import com.academy.fintech.pe.public_interface.agreement.AgreementMapper;
import com.academy.fintech.pe.public_interface.agreement.dto.AgreementActivationDto;
import com.academy.fintech.pe.public_interface.agreement.dto.AgreementDto;
import com.academy.fintech.pe.public_interface.agreement.dto.PaymentScheduleDto;
import com.academy.fintech.pe.public_interface.agreement.dto.PaymentSchedulePaymentDto;
import com.academy.fintech.pe.public_interface.agreement.exception.InvalidParametersException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AgreementCreationServiceTest {

    @Mock
    private ProductService productService;

    @Mock
    private AgreementService agreementService;

    @Spy
    private final AgreementMapper agreementMapper = Mappers.getMapper(AgreementMapper.class);

    @InjectMocks
    private AgreementCreationServiceImpl agreementCreationService;

    private static Product product;

    @BeforeEach
    void setUpProduct() {
        product = Product.builder()
                .code("testProductCode")
                .interest_min(BigDecimal.valueOf(5))
                .interest_max(BigDecimal.valueOf(10))
                .loan_term_min(12)
                .loan_term_max(24)
                .origination_amount_min(BigDecimal.valueOf(5000))
                .origination_amount_max(BigDecimal.valueOf(10000))
                .principal_amount_min(BigDecimal.valueOf(50000))
                .principal_amount_max(BigDecimal.valueOf(100000))
                .build();
    }

    @ParameterizedTest(name = "{index} - {0}")
    @MethodSource("provideArgumentsForCreateAgreement_whenAgreementIsInvalidTest")
    void createAgreement_whenAgreementIsInvalid(String ignoredTestName, Product testProduct, AgreementDto agreementDto) {
        when(productService.getProduct(testProduct.getCode())).thenReturn(testProduct);

        assertThatExceptionOfType(InvalidParametersException.class)
                .isThrownBy(() -> agreementCreationService.createAgreement(agreementDto));
    }

    @Test
    void createAgreement_whenAgreementIsValid() {
        UUID expectedAgreementNumber = UUID.randomUUID();
        AgreementDto agreementDto = AgreementDto.builder()
                .productCode(product.getCode())
                .clientId(UUID.randomUUID())
                .interest(BigDecimal.valueOf(5))
                .loanTerm(12)
                .originationAmount(BigDecimal.valueOf(5000))
                .disbursementAmount(BigDecimal.valueOf(50000))
                .build();
        when(productService.getProduct(product.getCode())).thenReturn(product);
        when(agreementService.saveAgreement(any(Agreement.class))).thenReturn(
                Agreement.builder()
                        .agreementNumber(expectedAgreementNumber)
                        .build()
        );

        assertThatNoException().isThrownBy(() -> {
            UUID actualAgreementNumber = agreementCreationService.createAgreement(agreementDto);
            assertThat(actualAgreementNumber).isEqualTo(expectedAgreementNumber);
        });
    }

    @Test
    void activateAgreement_whenAgreementIsAlreadyActive() {
        Agreement agreement = Agreement.builder()
                .agreementNumber(UUID.randomUUID())
                .status(AgreementStatus.ACTIVE)
                .build();
        AgreementActivationDto agreementActivationDto = AgreementActivationDto.builder()
                .agreementNumber(agreement.getAgreementNumber())
                .build();
        when(agreementService.getAgreement(agreement.getAgreementNumber())).thenReturn(agreement);

        assertThatExceptionOfType(InvalidParametersException.class)
                .isThrownBy(() -> agreementCreationService.activateAgreement(agreementActivationDto));
    }

    @Test
    void activateAgreement() {
        Agreement agreement = Agreement.builder()
                .agreementNumber(UUID.randomUUID())
                .clientId(UUID.randomUUID())
                .product(product)
                .originationAmount(BigDecimal.valueOf(10000))
                .principalAmount(BigDecimal.valueOf(200000))
                .interest(BigDecimal.valueOf(15))
                .loanTerm(12)
                .status(AgreementStatus.NEW)
                .paymentSchedules(new ArrayList<>())
                .build();
        LocalDateTime disbursementDate = LocalDateTime.now();
        AgreementActivationDto agreementActivationDto = AgreementActivationDto.builder()
                .agreementNumber(agreement.getAgreementNumber())
                .disbursementDate(disbursementDate)
                .build();
        when(agreementService.getAgreement(agreement.getAgreementNumber())).thenReturn(agreement);

        PaymentScheduleDto paymentScheduleDto = agreementCreationService.activateAgreement(agreementActivationDto);

        assertThat(paymentScheduleDto).isNotNull();
        assertThat(paymentScheduleDto.version()).isEqualTo(1);
        assertThat(paymentScheduleDto.payments()).hasSize(agreement.getLoanTerm());
        PaymentSchedulePaymentDto paymentSchedulePaymentDto = paymentScheduleDto.payments().get(0);
        assertThat(paymentSchedulePaymentDto).isNotNull();
        assertThat(paymentSchedulePaymentDto.paymentDate()).isEqualTo(disbursementDate.plusMonths(1));
        verify(agreementService).saveAgreement(agreement);
    }

    private static Stream<Arguments> provideArgumentsForCreateAgreement_whenAgreementIsInvalidTest() {
        return Stream.of(
                Arguments.of(
                        "Invalid interest",
                        product,
                        AgreementDto.builder()
                                .productCode(product.getCode())
                                .clientId(UUID.randomUUID())
                                .interest(BigDecimal.valueOf(4))
                                .loanTerm(12)
                                .originationAmount(BigDecimal.valueOf(5000))
                                .disbursementAmount(BigDecimal.valueOf(50000))
                                .build()
                ),
                Arguments.of(
                        "Invalid interest",
                        product,
                        AgreementDto.builder()
                                .productCode(product.getCode())
                                .clientId(UUID.randomUUID())
                                .interest(BigDecimal.valueOf(12))
                                .loanTerm(12)
                                .originationAmount(BigDecimal.valueOf(5000))
                                .disbursementAmount(BigDecimal.valueOf(50000))
                                .build()
                ),
                Arguments.of(
                        "Invalid loan term",
                        product,
                        AgreementDto.builder()
                                .productCode(product.getCode())
                                .clientId(UUID.randomUUID())
                                .interest(BigDecimal.valueOf(5))
                                .loanTerm(10)
                                .originationAmount(BigDecimal.valueOf(5000))
                                .disbursementAmount(BigDecimal.valueOf(50000))
                                .build()
                ),
                Arguments.of(
                        "Invalid loan term",
                        product,
                        AgreementDto.builder()
                                .productCode(product.getCode())
                                .clientId(UUID.randomUUID())
                                .interest(BigDecimal.valueOf(5))
                                .loanTerm(25)
                                .originationAmount(BigDecimal.valueOf(5000))
                                .disbursementAmount(BigDecimal.valueOf(50000))
                                .build()
                ),
                Arguments.of(
                        "Invalid origination amount",
                        product,
                        AgreementDto.builder()
                                .productCode(product.getCode())
                                .clientId(UUID.randomUUID())
                                .interest(BigDecimal.valueOf(5))
                                .loanTerm(12)
                                .originationAmount(BigDecimal.valueOf(4000))
                                .disbursementAmount(BigDecimal.valueOf(50000))
                                .build()
                ),
                Arguments.of(
                        "Invalid origination amount",
                        product,
                        AgreementDto.builder()
                                .productCode(product.getCode())
                                .clientId(UUID.randomUUID())
                                .interest(BigDecimal.valueOf(5))
                                .loanTerm(12)
                                .originationAmount(BigDecimal.valueOf(20000))
                                .disbursementAmount(BigDecimal.valueOf(50000))
                                .build()
                ),
                Arguments.of(
                        "Invalid principal amount",
                        product,
                        AgreementDto.builder()
                                .productCode(product.getCode())
                                .clientId(UUID.randomUUID())
                                .interest(BigDecimal.valueOf(5))
                                .loanTerm(12)
                                .originationAmount(BigDecimal.valueOf(5000))
                                .disbursementAmount(BigDecimal.valueOf(40000))
                                .build()
                ),
                Arguments.of(
                        "Invalid principal amount",
                        product,
                        AgreementDto.builder()
                                .productCode(product.getCode())
                                .clientId(UUID.randomUUID())
                                .interest(BigDecimal.valueOf(5))
                                .loanTerm(12)
                                .originationAmount(BigDecimal.valueOf(5000))
                                .disbursementAmount(BigDecimal.valueOf(100000))
                                .build()
                )
        );
    }
}