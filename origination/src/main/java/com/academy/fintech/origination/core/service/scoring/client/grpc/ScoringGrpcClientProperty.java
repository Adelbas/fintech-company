package com.academy.fintech.origination.core.service.scoring.client.grpc;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "api.client.scoring.grpc")
public record ScoringGrpcClientProperty(String host, int port) { }
