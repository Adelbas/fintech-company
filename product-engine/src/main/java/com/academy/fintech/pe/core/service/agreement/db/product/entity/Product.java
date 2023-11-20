package com.academy.fintech.pe.core.service.agreement.db.product.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @Column(name = "code")
    private String code;

    @Column(name = "loan_term_min", nullable = false)
    private Integer loan_term_min;

    @Column(name = "loan_term_max", nullable = false)
    private Integer loan_term_max;

    @Column(name = "principal_amount_min", nullable = false)
    private BigDecimal principal_amount_min;

    @Column(name = "principal_amount_max", nullable = false)
    private BigDecimal principal_amount_max;

    @Column(name = "interest_min", nullable = false)
    private BigDecimal interest_min;

    @Column(name = "interest_max", nullable = false)
    private BigDecimal interest_max;

    @Column(name = "origination_amount_min", nullable = false)
    private BigDecimal origination_amount_min;

    @Column(name = "origination_amount_max", nullable = false)
    private BigDecimal origination_amount_max;
}