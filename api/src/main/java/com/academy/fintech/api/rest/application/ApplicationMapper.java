package com.academy.fintech.api.rest.application;

import com.academy.fintech.api.public_interface.application.dto.ApplicationDto;
import com.academy.fintech.api.rest.dto.ApplicationRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ApplicationMapper {
    ApplicationDto toApplicationDto(ApplicationRequest request);

}
