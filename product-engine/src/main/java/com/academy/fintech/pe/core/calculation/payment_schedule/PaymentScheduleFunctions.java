package com.academy.fintech.pe.core.calculation.payment_schedule;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

/**
 * Represents functions for payment schedule
 */
public class PaymentScheduleFunctions {

    /**
     * Realization of PMT algorithm
     *
     * @param principalAmount loan principal amount
     * @param interest        loan interest
     * @param term            loan termination in months
     * @return payment value for each period
     */
    public static BigDecimal calculatePMT(BigDecimal principalAmount, BigDecimal interest, int term) {
        BigDecimal periodicInterestRate = calculatePeriodicInterestRate(interest);

        BigDecimal annuityRate = (periodicInterestRate.add(BigDecimal.ONE)).pow(term);

        BigDecimal multiplier = annuityRate.divide(annuityRate.subtract(BigDecimal.ONE), RoundingMode.HALF_UP);

        return principalAmount.multiply(periodicInterestRate).multiply(multiplier).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Realization of IPMT algorithm
     *
     * @param principalAmount loan principal amount
     * @param interest        loan interest
     * @param periodPayment   loan period payment (PMT)
     * @param period          period number to calculate
     * @return interest payment value for current period
     */
    public static BigDecimal calculateIPMT(BigDecimal principalAmount, BigDecimal interest, BigDecimal periodPayment, int period) {
        BigDecimal periodicInterestRate = calculatePeriodicInterestRate(interest);

        BigDecimal periodAnnuityRate = (periodicInterestRate.add(BigDecimal.ONE)).pow(period - 1);

        BigDecimal multiplier = principalAmount.multiply(periodicInterestRate).setScale(2, RoundingMode.HALF_UP).subtract(periodPayment);

        return periodPayment.add((periodAnnuityRate.multiply(multiplier))).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Realization of PPMT algorithm
     *
     * @param periodPayment   period payment (PMT)
     * @param interestPayment interest payment (IPMT)
     * @return principal payment for current period
     */
    public static BigDecimal calculatePPMT(BigDecimal periodPayment, BigDecimal interestPayment) {
        return periodPayment.subtract(interestPayment).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Calculates next payment day by adding one month to previous payment date
     *
     * @param previousPaymentDate previous payment date
     * @return next payment date
     */
    public static LocalDateTime calculateNextPaymentDate(LocalDateTime previousPaymentDate) {
        return previousPaymentDate.plusMonths(1);
    }

    /**
     * Calculates interest rate for each period
     *
     * @param interest loan interest
     * @return interest for each period
     */
    private static BigDecimal calculatePeriodicInterestRate(BigDecimal interest) {
        return interest.divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP).divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP);
    }
}
