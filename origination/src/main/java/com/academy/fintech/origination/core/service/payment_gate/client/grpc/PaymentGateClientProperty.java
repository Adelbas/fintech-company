package com.academy.fintech.origination.core.service.payment_gate.client.grpc;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "api.client.payment-gate.grpc")
public record PaymentGateClientProperty(String host, int port) { }
