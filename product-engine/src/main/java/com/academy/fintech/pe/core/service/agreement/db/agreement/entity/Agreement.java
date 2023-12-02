package com.academy.fintech.pe.core.service.agreement.db.agreement.entity;

import com.academy.fintech.pe.core.service.agreement.db.agreement.entity.enums.AgreementStatus;
import com.academy.fintech.pe.core.service.agreement.db.payment_schedule.entity.PaymentSchedule;
import com.academy.fintech.pe.core.service.agreement.db.product.entity.Product;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@ToString(exclude = "paymentSchedules")
@EqualsAndHashCode(exclude = "paymentSchedules")
@NoArgsConstructor
@AllArgsConstructor
public class Agreement {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "agreement_number")
    private UUID agreementNumber;

    @Column(name = "client_id", nullable = false)
    private UUID clientId;

    @ManyToOne
    @JoinColumn(name = "product_code", nullable = false)
    private Product product;

    @Column(name = "loan_term", nullable = false)
    private Integer loanTerm;

    @Column(name = "principal_amount", nullable = false)
    private BigDecimal principalAmount;

    @Column(name = "origination_amount", nullable = false)
    private BigDecimal originationAmount;

    @Column(name = "interest", nullable = false)
    private BigDecimal interest;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AgreementStatus status;

    @Column(name = "disbursement_date")
    private LocalDateTime disbursementDate;

    @Column(name = "next_payment_date")
    private LocalDateTime nextPaymentDate;

    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "agreement")
    private List<PaymentSchedule> paymentSchedules;
}

