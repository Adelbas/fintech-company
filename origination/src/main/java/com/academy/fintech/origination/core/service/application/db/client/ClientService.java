package com.academy.fintech.origination.core.service.application.db.client;

import com.academy.fintech.origination.core.service.application.db.client.entity.Client;
import com.academy.fintech.origination.public_interface.application.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    public void saveClient(Client client) {
        clientRepository.save(client);
    }

    public Client getClient(UUID clientId) {
        return clientRepository.findById(clientId).orElseThrow(() -> new NotFoundException("Client not found"));
    }

    public Client getOrCreate(String email, String firstName, String lastName, BigDecimal salary) {
        Optional<Client> optionalClient = clientRepository.findByEmail(email);

        return optionalClient.orElseGet(
                () -> clientRepository.save(
                        Client.builder()
                                .firstName(firstName)
                                .lastName(lastName)
                                .email(email)
                                .salary(salary)
                                .build()
                )
        );
    }
    public Optional<Client> findClient(String email) {
        return clientRepository.findByEmail(email);
    }
}
