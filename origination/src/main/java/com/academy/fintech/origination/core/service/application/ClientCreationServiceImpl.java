package com.academy.fintech.origination.core.service.application;

import com.academy.fintech.origination.core.service.application.db.client.ClientService;
import com.academy.fintech.origination.core.service.application.db.client.entity.Client;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Represents client creation service implementation.
 * Uses {@link ClientService} to interact with database.
 */
@Service
@RequiredArgsConstructor
public class ClientCreationServiceImpl implements ClientCreationService {

    private final ClientService clientService;

    /**
     * Provides client creation.
     * Saves created client to database.
     *
     * @param firstName first name
     * @param lastName last name
     * @param email email
     * @param salary salary
     * @return created client
     */
    @Override
    public Client createClient(String firstName, String lastName, String email, BigDecimal salary) {
        Client client = Client.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .salary(salary)
                .build();

        clientService.saveClient(client);
        return client;
    }
}
