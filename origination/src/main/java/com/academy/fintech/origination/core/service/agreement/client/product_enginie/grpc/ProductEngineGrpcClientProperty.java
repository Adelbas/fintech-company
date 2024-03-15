package com.academy.fintech.origination.core.service.agreement.client.product_enginie.grpc;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "api.client.product-engine.grpc")
public record ProductEngineGrpcClientProperty(String host, int port) { }
