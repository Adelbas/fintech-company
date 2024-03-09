package com.academy.fintech.origination.public_interface.application;

import com.academy.fintech.origination.public_interface.application.dto.ApplicationDto;
import com.academy.fintech.origination.public_interface.application.dto.CancelApplicationDto;
import jakarta.validation.Valid;

import java.util.UUID;

/**
 * Interface that provides creation and cancellation of application
 */
public interface ApplicationService {
    UUID createApplication(@Valid ApplicationDto applicationDto);

    boolean cancelApplication(CancelApplicationDto cancelApplicationDto);
}
