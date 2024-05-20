package com.academy.fintech.pg.core.service.payment.client.product_engine;

import com.academy.fintech.pg.core.service.payment.client.product_engine.grpc.ProductEngineGrpcClient;
import com.academy.fintech.pg.public_interface.payment.dto.PaymentRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductEngineClientService {

    private final ProductEngineGrpcClient productEngineGrpcClient;

    private final ProductEngineGrpcMapper productEngineGrpcMapper;

    public void handleLoanPayment(PaymentRequestDto paymentRequestDto) {
        productEngineGrpcClient.handleLoanPayment(
                productEngineGrpcMapper.toLoanPaymentRequest(paymentRequestDto)
        );
    }
}
