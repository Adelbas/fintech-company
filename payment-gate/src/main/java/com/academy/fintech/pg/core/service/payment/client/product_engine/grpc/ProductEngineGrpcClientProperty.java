package com.academy.fintech.pg.core.service.payment.client.product_engine.grpc;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "api.client.product-engine.grpc")
public record ProductEngineGrpcClientProperty(String host, int port) { }
