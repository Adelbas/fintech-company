package com.academy.fintech.origination.core.service.application;

import com.academy.fintech.origination.core.service.application.db.client.ClientService;
import com.academy.fintech.origination.core.service.application.db.client.entity.Client;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.only;

@ExtendWith(MockitoExtension.class)
public class ClientCreationServiceTest {

    @Mock
    private ClientService clientService;

    @InjectMocks
    private ClientCreationServiceImpl clientCreationServiceImpl;

    @Test
    void createClient() {
        Client expectedClient = Client.builder()
                .firstName("firstName")
                .lastName("lastName")
                .email("test@mail.ru")
                .salary(BigDecimal.valueOf(1234))
                .build();

        Client actualClient = clientCreationServiceImpl.createClient(
                expectedClient.getFirstName(),
                expectedClient.getLastName(),
                expectedClient.getEmail(),
                expectedClient.getSalary()
        );

        assertThat(actualClient).isEqualTo(expectedClient);
        verify(clientService, only()).saveClient(expectedClient);
    }
}