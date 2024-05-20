package com.academy.fintech.mp.core.service.payment.client.payment_gate.rest;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "api.client.payment-gate.rest")
public record PaymentGateClientProperty(String host, int port) { }
