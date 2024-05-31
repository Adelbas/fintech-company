package com.academy.fintech.dwh.core.service.agreement.db.entity;

import com.academy.fintech.dwh.core.service.agreement.db.entity.enums.AgreementStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgreementHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "agreement_number", nullable = false)
    private UUID agreementNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AgreementStatus status;

    @Column(name = "business_date", nullable = false)
    private LocalDateTime businessDate;

    @Column(name = "idempotency_key")
    private UUID idempotencyKey;
}
