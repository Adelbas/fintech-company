package com.academy.fintech.pe.core.service.agreement.db.payment_schedule.entity;

import com.academy.fintech.pe.core.service.agreement.db.agreement.entity.Agreement;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@ToString(exclude = "payments")
@EqualsAndHashCode(exclude = "payments")
@NoArgsConstructor
@AllArgsConstructor
public class PaymentSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_schedule_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "agreement_number", nullable = false)
    private Agreement agreement;

    @Column(name = "version", nullable = false)
    private Integer version;

    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "paymentSchedule")
    private List<PaymentSchedulePayment> payments;
}