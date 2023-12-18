package com.academy.fintech.origination.core.service.email;

import com.academy.fintech.origination.public_interface.application.dto.ApplicationEmailDto;

/**
 * Interface that provides sending email
 */
public interface EmailService {
    void sendApplicationApprovedEmail(ApplicationEmailDto applicationEmailDto);
    void sendApplicationRejectedEmail(ApplicationEmailDto applicationEmailDto);
}
