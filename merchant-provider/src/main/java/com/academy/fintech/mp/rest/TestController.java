package com.academy.fintech.mp.rest;

import com.academy.fintech.mp.server.api.TestApi;
import com.academy.fintech.mp.core.service.payment.db.entity.enums.PaymentStatus;
import com.academy.fintech.mp.public_interface.payment.PaymentService;
import com.academy.fintech.mp.public_interface.payment.dto.PaymentStatusUpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * Controller for testing.
 * Represents payment completion and status update
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class TestController implements TestApi {

    private final PaymentService paymentService;

    @Override
    public ResponseEntity<Void> updateStatus(UUID id, com.academy.fintech.mp.server.model.PaymentStatus status) {
        log.info("Got status update request:\nid: {}\nstatus: {}",id, status);
        paymentService.updateStatus(
                PaymentStatusUpdateDto.builder()
                        .paymentId(id)
                        .status(PaymentStatus.valueOf(status.name()))
                        .build()
        );

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
