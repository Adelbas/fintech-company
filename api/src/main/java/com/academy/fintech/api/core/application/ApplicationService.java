package com.academy.fintech.api.core.application;

import com.academy.fintech.api.core.origination.client.OriginationClientService;
import com.academy.fintech.api.public_interface.application.dto.ApplicationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Represent application service.
 * Uses {@link OriginationClientService} to interact with Origination service
 */
@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final OriginationClientService originationClientService;

    public String createApplication(ApplicationDto applicationDto) {
        return originationClientService.createApplication(applicationDto);
    }

}
