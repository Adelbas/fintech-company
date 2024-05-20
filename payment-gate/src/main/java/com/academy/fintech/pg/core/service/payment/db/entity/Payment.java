package com.academy.fintech.pg.core.service.payment.db.entity;

import com.academy.fintech.pg.core.service.payment.db.entity.enums.PaymentStatus;
import com.academy.fintech.pg.core.service.payment.db.entity.enums.PaymentType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode(exclude = {
        "countOfStatusChecks",
        "paymentStatusNextCheckDate",
        "paymentStatusUpdateDate"
})
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "client_email", nullable = false)
    private String clientEmail;

    @Column(name = "agreement_number", nullable = false)
    private UUID agreementNumber;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private PaymentType paymentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PaymentStatus paymentStatus;

    @Column(name = "payment_date", nullable = false)
    private LocalDateTime paymentDate;

    @Column(name = "status_check_external_id", nullable = false, unique = true)
    private UUID statusCheckExternalId;

    @Column(name = "count_of_status_check", nullable = false)
    private Integer countOfStatusChecks;

    @Column(name = "next_status_check_date", nullable = false)
    private LocalDateTime paymentStatusNextCheckDate;

    @Column(name = "status_update_date")
    private LocalDateTime paymentStatusUpdateDate;
}
