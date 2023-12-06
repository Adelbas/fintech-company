package com.academy.fintech.api.core.origination.client;

import com.academy.fintech.api.public_interface.application.dto.ApplicationDto;
import com.academy.fintech.application.ApplicationRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OriginationGrpcMapper {

    @Mapping(target = "disbursementAmount", source = "amount")
    ApplicationRequest toApplicationRequest(ApplicationDto applicationDto);
}
