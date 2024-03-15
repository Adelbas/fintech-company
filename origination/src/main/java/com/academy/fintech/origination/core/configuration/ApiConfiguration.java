package com.academy.fintech.origination.core.configuration;

import com.academy.fintech.origination.core.service.agreement.client.product_enginie.grpc.ProductEngineGrpcClientProperty;
import com.academy.fintech.origination.core.service.payment_gate.client.grpc.PaymentGateClientProperty;
import com.academy.fintech.origination.core.service.scoring.client.grpc.ScoringGrpcClientProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({ ScoringGrpcClientProperty.class, ProductEngineGrpcClientProperty.class, PaymentGateClientProperty.class})
public class ApiConfiguration { }
