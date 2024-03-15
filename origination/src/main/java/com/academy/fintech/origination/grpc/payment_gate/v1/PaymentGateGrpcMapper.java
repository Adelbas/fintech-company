package com.academy.fintech.origination.grpc.payment_gate.v1;

import com.academy.fintech.origination.public_interface.payment_gate.dto.DisbursementStatusUpdateDto;
import com.academy.fintech.pg.DisbursementStatusRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentGateGrpcMapper {
    DisbursementStatusUpdateDto toDisbursementStatusUpdateDto(DisbursementStatusRequest disbursementStatusRequest);
}
