package com.academy.fintech.origination.core.service.application.db.application;

import com.academy.fintech.origination.core.service.application.db.application.entity.Application;
import com.academy.fintech.origination.core.service.application.db.application.entity.enums.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public interface ApplicationRepository extends JpaRepository<Application, UUID> {

    Optional<Application> findApplicationByClientClientIdAndStatusAndRequestedDisbursementAmount(UUID clientId,
                                                                                                 ApplicationStatus applicationStatus,
                                                                                                 BigDecimal requestedDisbursementAmount);
}
