package com.academy.fintech.origination.core.service.application;

import com.academy.fintech.origination.core.service.application.db.client.entity.Client;

import java.math.BigDecimal;

/**
 * Interface that provides client creation
 */
public interface ClientCreationService {
    Client createClient(String firstName, String lastName, String email, BigDecimal salary);
}
