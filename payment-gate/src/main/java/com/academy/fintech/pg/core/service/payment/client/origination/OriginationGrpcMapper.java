package com.academy.fintech.pg.core.service.payment.client.origination;

import com.academy.fintech.pg.DisbursementStatusRequest;
import com.academy.fintech.pg.public_interface.payment.dto.UpdatePaymentStatusDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OriginationGrpcMapper {

    DisbursementStatusRequest toDisbursementStatusRequest(UpdatePaymentStatusDto updatePaymentStatusDto);
}
