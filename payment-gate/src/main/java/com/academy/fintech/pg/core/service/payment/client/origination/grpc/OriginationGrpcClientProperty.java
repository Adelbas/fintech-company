package com.academy.fintech.pg.core.service.payment.client.origination.grpc;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "api.client.origination.grpc")
public record OriginationGrpcClientProperty(String host, int port) { }
