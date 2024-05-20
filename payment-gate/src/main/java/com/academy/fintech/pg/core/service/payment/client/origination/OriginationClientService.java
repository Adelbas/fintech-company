package com.academy.fintech.pg.core.service.payment.client.origination;

import com.academy.fintech.pg.DisbursementStatusRequest;
import com.academy.fintech.pg.core.service.payment.client.origination.grpc.OriginationGrpcClient;
import com.academy.fintech.pg.core.service.payment.db.entity.enums.PaymentStatus;
import com.academy.fintech.pg.public_interface.payment.dto.UpdatePaymentStatusDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OriginationClientService {

    private final OriginationGrpcClient originationGrpcClient;

    private final OriginationGrpcMapper originationGrpcMapper;

    public void updatePaymentStatus(UpdatePaymentStatusDto updatePaymentStatusDto){
        log.info("Sending updateStatusRequest to Origination: {}", updatePaymentStatusDto);
        boolean isCompletedSuccessfully = PaymentStatus.COMPLETED.equals(updatePaymentStatusDto.status());

        originationGrpcClient.updateDisbursementStatus(
                DisbursementStatusRequest.newBuilder()
                        .setAgreementNumber(updatePaymentStatusDto.agreementNumber().toString())
                        .setIsDisbursementCompletedSuccessfully(isCompletedSuccessfully)
                        .setDisbursementDate(updatePaymentStatusDto.completionDate().toString())
                        .build()
        );
    }
}
