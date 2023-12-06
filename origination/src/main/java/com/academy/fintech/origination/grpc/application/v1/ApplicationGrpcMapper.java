package com.academy.fintech.origination.grpc.application.v1;

import com.academy.fintech.application.ApplicationRequest;
import com.academy.fintech.application.CancelApplicationRequest;
import com.academy.fintech.origination.public_interface.application.dto.ApplicationDto;
import com.academy.fintech.origination.public_interface.application.dto.CancelApplicationDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ApplicationGrpcMapper {

    @Mapping(target = "requestedDisbursementAmount", source = "disbursementAmount")
    ApplicationDto toApplicationDto(ApplicationRequest applicationRequest);

    CancelApplicationDto toCancelApplicationDto(CancelApplicationRequest cancelApplicationRequest);
}