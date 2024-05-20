package com.academy.fintech.mp.configuration;

import com.academy.fintech.mp.core.service.payment.client.payment_gate.rest.PaymentGateClientProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({ PaymentGateClientProperty.class})
public class ApiConfiguration { }
