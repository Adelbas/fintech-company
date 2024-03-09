package com.academy.fintech.origination.public_interface.scoring;

import com.academy.fintech.origination.core.service.application.db.application.entity.Application;

/**
 * Interface that provides scoring the application
 */
public interface ScoringService {
    void scoreApplication(Application application);
}
