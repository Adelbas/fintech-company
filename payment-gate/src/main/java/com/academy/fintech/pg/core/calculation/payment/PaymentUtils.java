package com.academy.fintech.pg.core.calculation.payment;

import java.time.LocalDateTime;

/**
 * Functions for calculating next date to send checking request to Provider.
 * Interval between requests increases with each request and calculating with formula:
 * interval = (current request number)^2 + 1
 */
public class PaymentUtils {

    public static LocalDateTime calculateNextCheckingDate() {
        return calculateNextCheckingDate(LocalDateTime.now(), 0);
    }

    public static LocalDateTime calculateNextCheckingDate(LocalDateTime previousCheckingDate, int countOfChecks) {
        int interval = getNextIntervalInMinutes(countOfChecks);

        LocalDateTime nextCheckingDate = previousCheckingDate.plusMinutes(interval);

        return nextCheckingDate;
    }

    private static int getNextIntervalInMinutes(int countOfChecks) {
        return countOfChecks * countOfChecks + 1;
    }
}
