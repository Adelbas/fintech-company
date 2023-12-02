package com.academy.fintech.pe.core.calculation.payment_schedule;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class PaymentScheduleCalculationsTest {

    @ParameterizedTest(name = "Test calculate PMT")
    @MethodSource("provideArgumentsForPMTTest")
    void calculatePMT(BigDecimal principalAmount, BigDecimal interest, int term, BigDecimal expectedPeriodPayment) {
        BigDecimal actualPeriodPayment = PaymentScheduleFunctions.calculatePMT(principalAmount, interest, term);

        assertThat(actualPeriodPayment).isEqualTo(expectedPeriodPayment);
    }

    @ParameterizedTest(name = "Test calculate IPMT")
    @MethodSource("provideArgumentsForIPMTTest")
    void calculateIPMT(BigDecimal principalAmount, BigDecimal interest, BigDecimal periodPayment, int period, BigDecimal expectedInterestPayment) {
        BigDecimal actualInterestPayment = PaymentScheduleFunctions.calculateIPMT(principalAmount, interest, periodPayment, period);

        assertThat(actualInterestPayment).isEqualTo(expectedInterestPayment);
    }

    @ParameterizedTest(name = "Test calculate PPMT")
    @MethodSource("provideArgumentsForPPMTTest")
    void calculatePPMT(BigDecimal periodPayment, BigDecimal interestPayment, BigDecimal expectedPrincipalPayment) {
        BigDecimal actualPrincipalPayment = PaymentScheduleFunctions.calculatePPMT(periodPayment, interestPayment);

        assertThat(actualPrincipalPayment).isEqualTo(expectedPrincipalPayment);
    }

    @ParameterizedTest(name = "Test calculate nextPaymentDate")
    @MethodSource("provideArgumentsForNextPaymentDateTest")
    void calculateNextPaymentDate(LocalDateTime previousPaymentDate, LocalDateTime expectedNextPaymentDate) {
        LocalDateTime actualNextPaymentDate = PaymentScheduleFunctions.calculateNextPaymentDate(previousPaymentDate);

        assertThat(actualNextPaymentDate).isEqualTo(expectedNextPaymentDate);
    }

    private static Stream<Arguments> provideArgumentsForPMTTest() {
        return Stream.of(
                Arguments.of(
                        BigDecimal.valueOf(500000),
                        BigDecimal.valueOf(8),
                        12,
                        BigDecimal.valueOf(43494.21)
                ),
                Arguments.of(
                        BigDecimal.valueOf(220000),
                        BigDecimal.valueOf(4.75),
                        30,
                        BigDecimal.valueOf(7791.85)
                ),
                Arguments.of(
                        BigDecimal.valueOf(2000000),
                        BigDecimal.valueOf(15),
                        60,
                        BigDecimal.valueOf(47579.86)
                )
        );
    }

    private static Stream<Arguments> provideArgumentsForIPMTTest() {
        return Stream.of(
                Arguments.of(
                        BigDecimal.valueOf(500000),
                        BigDecimal.valueOf(8),
                        BigDecimal.valueOf(43494.21),
                        1,
                        BigDecimal.valueOf(3333.33)
                ),
                Arguments.of(
                        BigDecimal.valueOf(500000),
                        BigDecimal.valueOf(8),
                        BigDecimal.valueOf(43494.21),
                        2,
                        BigDecimal.valueOf(3065.59)
                ),
                Arguments.of(
                        BigDecimal.valueOf(500000),
                        BigDecimal.valueOf(8),
                        BigDecimal.valueOf(43494.21),
                        3,
                        BigDecimal.valueOf(2796.07)
                ),
                Arguments.of(
                        BigDecimal.valueOf(500000),
                        BigDecimal.valueOf(8),
                        BigDecimal.valueOf(43494.21),
                        4,
                        BigDecimal.valueOf(2524.75)
                ),
                Arguments.of(
                        BigDecimal.valueOf(500000),
                        BigDecimal.valueOf(8),
                        BigDecimal.valueOf(43494.21),
                        5,
                        BigDecimal.valueOf(2251.62)
                ),
                Arguments.of(
                        BigDecimal.valueOf(500000),
                        BigDecimal.valueOf(8),
                        BigDecimal.valueOf(43494.21),
                        7,
                        BigDecimal.valueOf(1699.88)
                ),
                Arguments.of(
                        BigDecimal.valueOf(500000),
                        BigDecimal.valueOf(8),
                        BigDecimal.valueOf(43494.21),
                        8,
                        BigDecimal.valueOf(1421.25)
                ),
                Arguments.of(
                        BigDecimal.valueOf(500000),
                        BigDecimal.valueOf(8),
                        BigDecimal.valueOf(43494.21),
                        11,
                        BigDecimal.valueOf(574.17)
                ),
                Arguments.of(
                        BigDecimal.valueOf(2000000),
                        BigDecimal.valueOf(15),
                        BigDecimal.valueOf(47580),
                        1,
                        new BigDecimal("25000.00")
                ),
                Arguments.of(
                        BigDecimal.valueOf(2000000),
                        BigDecimal.valueOf(15),
                        BigDecimal.valueOf(47580),
                        2,
                        BigDecimal.valueOf(24717.75)
                ),
                Arguments.of(
                        BigDecimal.valueOf(2000000),
                        BigDecimal.valueOf(15),
                        BigDecimal.valueOf(47580),
                        3,
                        BigDecimal.valueOf(24431.97)
                ),
                Arguments.of(
                        BigDecimal.valueOf(2000000),
                        BigDecimal.valueOf(15),
                        BigDecimal.valueOf(47580),
                        4,
                        BigDecimal.valueOf(24142.62)
                )
        );
    }

    private static Stream<Arguments> provideArgumentsForPPMTTest() {
        return Stream.of(
                Arguments.of(
                        BigDecimal.valueOf(43494.21),
                        BigDecimal.valueOf(3333.33),
                        BigDecimal.valueOf(40160.88)
                ),
                Arguments.of(
                        BigDecimal.valueOf(43494.21),
                        BigDecimal.valueOf(3065.59),
                        BigDecimal.valueOf(40428.62)
                ),
                Arguments.of(
                        BigDecimal.valueOf(43494.21),
                        BigDecimal.valueOf(2796.07),
                        BigDecimal.valueOf(40698.14)
                ),
                Arguments.of(
                        BigDecimal.valueOf(43494.21),
                        BigDecimal.valueOf(2524.75),
                        BigDecimal.valueOf(40969.46)
                ),
                Arguments.of(
                        BigDecimal.valueOf(43494.21),
                        BigDecimal.valueOf(2251.62),
                        BigDecimal.valueOf(41242.59)
                ),
                Arguments.of(
                        BigDecimal.valueOf(47580),
                        BigDecimal.valueOf(25000),
                        new BigDecimal("22580.00")
                ),
                Arguments.of(
                        BigDecimal.valueOf(47580),
                        BigDecimal.valueOf(24717.75),
                        BigDecimal.valueOf(22862.25)
                ),
                Arguments.of(
                        BigDecimal.valueOf(47580),
                        BigDecimal.valueOf(24431.97),
                        BigDecimal.valueOf(23148.03)
                ),
                Arguments.of(
                        BigDecimal.valueOf(47580),
                        BigDecimal.valueOf(24142.62),
                        BigDecimal.valueOf(23437.38)
                )
        );
    }

    private static Stream<Arguments> provideArgumentsForNextPaymentDateTest() {
        return Stream.of(
                Arguments.of(
                        LocalDateTime.parse("2023-01-31T10:00:00"),
                        LocalDateTime.parse("2023-02-28T10:00:00")
                ),
                Arguments.of(
                        LocalDateTime.parse("2023-02-28T10:00:00"),
                        LocalDateTime.parse("2023-03-28T10:00:00")
                ),
                Arguments.of(
                        LocalDateTime.parse("2023-03-01T10:00:00"),
                        LocalDateTime.parse("2023-04-01T10:00:00")
                ),
                Arguments.of(
                        LocalDateTime.parse("2023-11-30T10:00:00"),
                        LocalDateTime.parse("2023-12-30T10:00:00")
                ),
                Arguments.of(
                        LocalDateTime.parse("2023-12-31T10:00:00"),
                        LocalDateTime.parse("2024-01-31T10:00:00")
                ),
                Arguments.of(
                        LocalDateTime.parse("2023-12-15T10:00:00"),
                        LocalDateTime.parse("2024-01-15T10:00:00")
                ),
                Arguments.of(
                        LocalDateTime.parse("2023-12-01T10:00:00"),
                        LocalDateTime.parse("2024-01-01T10:00:00")
                )
        );
    }
}