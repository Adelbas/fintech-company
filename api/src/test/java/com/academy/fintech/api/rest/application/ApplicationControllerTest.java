package com.academy.fintech.api.rest.application;

import com.academy.fintech.api.core.origination.client.grpc.OriginationGrpcClient;
import com.academy.fintech.api.rest.dto.ApplicationRequest;
import com.academy.fintech.application.ApplicationResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles(value = "test")
@AutoConfigureMockMvc
public class ApplicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OriginationGrpcClient originationGrpcClient;

    @Test
    void createApplication() throws Exception {
        String expectedApplicationId = UUID.randomUUID().toString();
        when(originationGrpcClient.createApplication(any(com.academy.fintech.application.ApplicationRequest.class)))
                .thenReturn(ApplicationResponse.newBuilder()
                        .setApplicationId(expectedApplicationId)
                        .build()
                );
        ApplicationRequest applicationRequest = ApplicationRequest.builder()
                .email("test@mail.ru")
                .firstName("firstname")
                .lastName("lastname")
                .salary(89000)
                .amount(40000)
                .build();
        var requestBuilder = post("/api/application")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(applicationRequest));

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().string(expectedApplicationId));
    }

    @Test
    void createApplication_whenApplicationIsExists() throws Exception {
        String expectedApplicationId = UUID.randomUUID().toString();
        Metadata metadata = new Metadata();
        metadata.put(Metadata.Key.of("application_id", Metadata.ASCII_STRING_MARSHALLER), expectedApplicationId);
        StatusRuntimeException statusRuntimeException = new StatusRuntimeException(Status.ALREADY_EXISTS, metadata);
        when(originationGrpcClient.createApplication(any(com.academy.fintech.application.ApplicationRequest.class)))
                .thenThrow(statusRuntimeException);
        ApplicationRequest applicationRequest = ApplicationRequest.builder()
                .email("test@mail.ru")
                .firstName("firstname")
                .lastName("lastname")
                .salary(89000)
                .amount(40000)
                .build();
        var requestBuilder = post("/api/application")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(applicationRequest));

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().string(expectedApplicationId));
    }
}