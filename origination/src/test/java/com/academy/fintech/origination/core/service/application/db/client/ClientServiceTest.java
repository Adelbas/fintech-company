package com.academy.fintech.origination.core.service.application.db.client;

import com.academy.fintech.origination.core.service.application.db.client.entity.Client;
import com.academy.fintech.origination.public_interface.application.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService;

    @Test
    void saveClient() {
        final Client client = mock(Client.class);
        when(clientRepository.save(client)).thenReturn(client);

        clientService.saveClient(client);

        verify(clientRepository, only()).save(client);
    }

    @Test
    void getClient_whenClientExists() {
        final UUID clientId = UUID.randomUUID();
        final Client expectedClient = mock(Client.class, withSettings().defaultAnswer(CALLS_REAL_METHODS));
        expectedClient.setClientId(clientId);
        when(clientRepository.findById(clientId)).thenReturn(Optional.of(expectedClient));

        Client actualClient = clientService.getClient(clientId);

        assertThat(actualClient).isEqualTo(expectedClient);
        verify(clientRepository, only()).findById(clientId);
    }

    @Test
    void getClient_whenClientNotExists() {
        final UUID clientId = UUID.randomUUID();
        when(clientRepository.findById(clientId)).thenReturn(Optional.empty());

        assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> clientService.getClient(clientId));
        verify(clientRepository, only()).findById(clientId);
    }

    @Test
    void findClient_whenClientExists() {
        String email = "test@mail.ru";
        Client expectedClient = Client.builder().email(email).build();
        when(clientRepository.findByEmail(email)).thenReturn(Optional.of(expectedClient));

        Optional<Client> actualClient = clientService.findClient(email);

        assertThat(actualClient)
                .isNotEmpty()
                .hasValue(expectedClient);
        verify(clientRepository, only()).findByEmail(email);
    }

    @Test
    void findClient_whenClientNotExists() {
        String email = "test@mail.ru";
        when(clientRepository.findByEmail(email)).thenReturn(Optional.empty());

        Optional<Client> actualClient = clientService.findClient(email);

        assertThat(actualClient).isEmpty();
        verify(clientRepository, only()).findByEmail(email);
    }
}