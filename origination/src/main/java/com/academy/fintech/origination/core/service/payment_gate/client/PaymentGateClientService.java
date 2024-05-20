package com.academy.fintech.origination.core.service.payment_gate.client;

import com.academy.fintech.origination.core.service.payment_gate.client.grpc.PaymentGateGrpcClient;
import com.academy.fintech.origination.public_interface.payment_gate.dto.PaymentRequestDto;
import com.academy.fintech.pg.PaymentRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentGateClientService {

    private final PaymentGateGrpcClient paymentGateGrpcClient;

    private final PaymentGateGrpcMapper paymentGateGrpcMapper;

    public void sendMoney(PaymentRequestDto paymentRequestDto) {
        PaymentRequest paymentRequest = paymentGateGrpcMapper.toPaymentRequest(paymentRequestDto);

        paymentGateGrpcClient.disburseAmount(paymentRequest);
    }
}
