package com.academy.fintech.mp.rest;

import com.academy.fintech.mp.server.api.PaymentsApi;
import com.academy.fintech.mp.server.model.LoanPaymentRequest;
import com.academy.fintech.mp.server.model.PaymentDisbursementRequest;
import com.academy.fintech.mp.server.model.PaymentResponse;
import com.academy.fintech.mp.server.model.StatusCheckResponse;
import com.academy.fintech.mp.public_interface.payment.dto.DisbursementRequestDto;
import com.academy.fintech.mp.public_interface.payment.dto.LoanPaymentRequestDto;
import com.academy.fintech.mp.public_interface.payment.dto.StatusCheckResponseDto;
import com.academy.fintech.mp.public_interface.payment.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PaymentController implements PaymentsApi {

    private final PaymentService paymentService;

    private final PaymentMapper paymentMapper;

    @Override
    public ResponseEntity<PaymentResponse> disburse(PaymentDisbursementRequest paymentDisbursementRequest) {
        log.info("Got disbursement payment request: {}", paymentDisbursementRequest);
        DisbursementRequestDto disbursementRequestDto = paymentMapper.toDisbursementRequestDto(paymentDisbursementRequest);

        UUID paymentExternalId = paymentService.disbursePayment(disbursementRequestDto);

        log.info("Returning paymentId: {}", paymentExternalId);

        return ResponseEntity.ok(
                PaymentResponse.builder()
                        .paymentId(paymentExternalId)
                        .build()
        );
    }

    @Override
    public ResponseEntity<StatusCheckResponse> getPaymentInfo(UUID id) {
        log.info("Got status check request of payment {}", id);
        StatusCheckResponseDto statusCheckResponseDto = paymentService.checkPaymentStatus(id);

        log.info("Returning status check response: {}", statusCheckResponseDto);
        return ResponseEntity.ok(paymentMapper.toStatusCheckResponse(statusCheckResponseDto));
    }

    @Override
    public ResponseEntity<PaymentResponse> makeLoanPayment(LoanPaymentRequest loanPaymentRequest) {
        LoanPaymentRequestDto loanPaymentRequestDto = paymentMapper.toLoanPaymentRequestDto(loanPaymentRequest);

        return ResponseEntity.ok(
                PaymentResponse.builder()
                        .paymentId(paymentService.loanPayment(loanPaymentRequestDto))
                        .build()
        );
    }
}
