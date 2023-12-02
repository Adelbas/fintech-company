package com.academy.fintech.pe.public_interface.agreement;

import com.academy.fintech.pe.core.service.agreement.db.payment_schedule.entity.PaymentSchedule;
import com.academy.fintech.pe.core.service.agreement.db.payment_schedule.entity.PaymentSchedulePayment;
import com.academy.fintech.pe.public_interface.agreement.dto.PaymentScheduleDto;
import com.academy.fintech.pe.public_interface.agreement.dto.PaymentSchedulePaymentDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AgreementMapper {

    @Mapping(target = "period", source = "periodNumber")
    @Mapping(target = "payment", source = "periodPayment")
    @Mapping(target = "interest", source = "interestPayment")
    @Mapping(target = "principal", source = "principalPayment")
    PaymentSchedulePaymentDto toPaymentSchedulePaymentDto(PaymentSchedulePayment paymentSchedulePayment);

    PaymentScheduleDto toPaymentScheduleDto(PaymentSchedule paymentSchedule);
}
