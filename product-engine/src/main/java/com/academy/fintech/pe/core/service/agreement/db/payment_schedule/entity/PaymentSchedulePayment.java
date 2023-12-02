package com.academy.fintech.pe.core.service.agreement.db.payment_schedule.entity;

import com.academy.fintech.pe.core.service.agreement.db.payment_schedule.entity.enums.PaymentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentSchedulePayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "payment_schedule_id", nullable = false)
    private PaymentSchedule paymentSchedule;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PaymentStatus status;

    @Column(name = "payment_date", nullable = false)
    private LocalDateTime paymentDate;

    @Column(name = "period_payment", nullable = false)
    private BigDecimal periodPayment;

    @Column(name = "interest_payment", nullable = false)
    private BigDecimal interestPayment;

    @Column(name = "principal_payment", nullable = false)
    private BigDecimal principalPayment;

    @Column(name = "period_number", nullable = false)
    private Integer periodNumber;

    @Transient
    private BigDecimal balance;
}