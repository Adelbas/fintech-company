package com.academy.fintech.mp.core.service.payment.client.payment_gate.rest;

import com.academy.fintech.mp.client.api.PaymentGateApi;
import org.springframework.stereotype.Component;

@Component
public class PaymentGateRestClient extends PaymentGateApi {

    public PaymentGateRestClient(PaymentGateClientProperty paymentGateClientProperty) {
        final String path = "http://" + paymentGateClientProperty.host() + ":" + paymentGateClientProperty.port();
        this.getApiClient().setBasePath(path);
    }
}