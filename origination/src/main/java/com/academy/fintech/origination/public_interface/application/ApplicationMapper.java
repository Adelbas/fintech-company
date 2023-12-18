package com.academy.fintech.origination.public_interface.application;

import com.academy.fintech.origination.core.service.application.db.application.entity.Application;
import com.academy.fintech.origination.public_interface.application.dto.ApplicationEmailDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ApplicationMapper {
    @Mapping(target = "email", source = "client.email")
    @Mapping(target = "firstName", source = "client.firstName")
    @Mapping(target = "lastName", source = "client.lastName")
    ApplicationEmailDto toApplicationEmailDto(Application application);
}
