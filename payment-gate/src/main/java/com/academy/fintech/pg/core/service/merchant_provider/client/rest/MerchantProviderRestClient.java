package com.academy.fintech.pg.core.service.merchant_provider.client.rest;

import com.academy.fintech.pg.client.api.PaymentsApi;
import org.springframework.stereotype.Component;

@Component
public class MerchantProviderRestClient extends PaymentsApi {

    public MerchantProviderRestClient(MerchantProviderRestClientProperty merchantProviderRestClientProperty) {
        final String path = "http://" + merchantProviderRestClientProperty.host() + ":" + merchantProviderRestClientProperty.port();
        this.getApiClient().setBasePath(path);
    }
}
