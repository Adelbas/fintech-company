package com.academy.fintech.pg.core.service.merchant_provider.client.rest;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "api.client.merchant-provider.rest")
public record MerchantProviderRestClientProperty(String host, int port) {
}
