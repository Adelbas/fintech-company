package com.academy.fintech.pg.core.configuration;

import com.academy.fintech.pg.core.service.merchant_provider.client.rest.MerchantProviderRestClientProperty;
import com.academy.fintech.pg.client.invoker.ApiClient;
import com.academy.fintech.pg.core.service.payment.client.origination.grpc.OriginationGrpcClientProperty;
import com.academy.fintech.pg.core.service.payment.client.product_engine.grpc.ProductEngineGrpcClientProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({ MerchantProviderRestClientProperty.class, OriginationGrpcClientProperty.class, ProductEngineGrpcClientProperty.class})
public class ApiConfiguration {

    @Bean
    public ApiClient apiClient() {
        return new ApiClient();
    }
}
