package com.academy.fintech.origination.core.service.agreement.db.agreement.entity;

import com.academy.fintech.origination.core.service.application.db.client.entity.Client;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Agreement {

    @Id
    @Column(name = "agreement_number")
    private UUID agreementNumber;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(name = "is_disbursement_completed")
    private boolean isDisbursementCompleted;
}
