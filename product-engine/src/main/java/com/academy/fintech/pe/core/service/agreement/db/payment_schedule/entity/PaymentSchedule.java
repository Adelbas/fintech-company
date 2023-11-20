package com.academy.fintech.pe.core.service.agreement.db.payment_schedule.entity;

import com.academy.fintech.pe.core.service.agreement.db.agreement.entity.Agreement;
import jakarta.persistence.*;
import lombok.*;

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

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "paymentSchedule")
    private List<PaymentSchedulePayment> payments;
}