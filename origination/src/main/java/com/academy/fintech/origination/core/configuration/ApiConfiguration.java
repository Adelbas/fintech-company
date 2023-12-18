package com.academy.fintech.origination.core.configuration;

import com.academy.fintech.origination.core.service.scoring.client.grpc.ScoringGrpcClientProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({ ScoringGrpcClientProperty.class })
public class ApiConfiguration { }
