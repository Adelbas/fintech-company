package com.academy.fintech.api.core.origination.client;

import com.academy.fintech.api.core.origination.client.grpc.OriginationGrpcClient;
import com.academy.fintech.api.public_interface.application.dto.ApplicationDto;
import com.academy.fintech.application.ApplicationRequest;
import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OriginationGrpcClientServiceTest {

    @Mock
    private OriginationGrpcClient originationGrpcClient;

    @Mock
    private OriginationGrpcMapperImpl originationGrpcMapper;

    @InjectMocks
    private OriginationClientService originationClientService;

    @Test
    void createApplication_stubThrowsAlreadyExistsException_shouldReturnApplicationIdFromTrailers() {
        String expectedApplicationId = UUID.randomUUID().toString();
        Metadata metadata = new Metadata();
        metadata.put(Metadata.Key.of("application_id",Metadata.ASCII_STRING_MARSHALLER), expectedApplicationId);
        StatusRuntimeException statusRuntimeException = new StatusRuntimeException(Status.ALREADY_EXISTS, metadata);
        when(originationGrpcMapper.toApplicationRequest(any(ApplicationDto.class))).thenReturn(mock(ApplicationRequest.class));
        when(originationGrpcClient.createApplication(any(ApplicationRequest.class))).thenThrow(statusRuntimeException);

        String actualApplicationId = originationClientService.createApplication(mock(ApplicationDto.class));

        assertThat(actualApplicationId).isEqualTo(expectedApplicationId);
    }
}