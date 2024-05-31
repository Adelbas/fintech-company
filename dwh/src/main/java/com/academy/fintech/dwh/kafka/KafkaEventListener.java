package com.academy.fintech.dwh.kafka;

import com.academy.fintech.dwh.core.service.kafka.DeadLetterService;
import com.academy.fintech.dwh.public_interface.agreement.AgreementHistoryService;
import com.academy.fintech.dwh.public_interface.agreement.dto.AgreementStatusUpdateDto;
import com.academy.fintech.dwh.public_interface.application.ApplicationHistoryService;
import com.academy.fintech.dwh.public_interface.application.dto.ApplicationStatusUpdateDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.TopicSuffixingStrategy;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaEventListener {

    private final AgreementHistoryService agreementHistoryService;

    private final ApplicationHistoryService applicationHistoryService;

    private final DeadLetterService deadLetterService;

    private final ObjectMapper objectMapper;

    @RetryableTopic(
            attempts = "4",
            topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE,
            backoff = @Backoff(
                    delay = 2000,
                    multiplier = 2,
                    maxDelay = 5000
            )
    )
    @KafkaListener(topics = {"${consumer.agreement-status.topic}"})
    public void processAgreementStatusEvent(ConsumerRecord<String, String> eventMessage, Acknowledgment ack) throws JsonProcessingException {
        log.info("Handle agreement status event for agreement {} from topic {}", eventMessage.key(), eventMessage.topic());

        try {
            AgreementStatusUpdateDto agreementStatusUpdateDto = objectMapper.readValue(eventMessage.value(), AgreementStatusUpdateDto.class);
            agreementHistoryService.handleAgreementStatusUpdate(agreementStatusUpdateDto);
            ack.acknowledge();
        } catch (Exception e) {
            log.error("Error handling agreement status event {}: {}", eventMessage.value(), e.getMessage());
            throw e;
        }
    }

    @RetryableTopic(
            attempts = "4",
            topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE,
            backoff = @Backoff(
                    delay = 2000,
                    multiplier = 2,
                    maxDelay = 5000
            )
    )
    @KafkaListener(topics = {"${consumer.application-status.topic}"})
    public void processApplicationStatusEvent(ConsumerRecord<String, String> eventMessage, Acknowledgment ack) throws JsonProcessingException {
        log.info("Handle application status event for application {} from topic {}", eventMessage.key(), eventMessage.topic());

        try {
            ApplicationStatusUpdateDto applicationStatusUpdateDto = objectMapper.readValue(eventMessage.value(), ApplicationStatusUpdateDto.class);
            applicationHistoryService.handleApplicationStatusUpdate(applicationStatusUpdateDto);
            ack.acknowledge();
        } catch (Exception e) {
            log.error("Error handling application status event {}: {}", eventMessage.value(), e.getMessage());
            throw e;
        }
    }

    @DltHandler
    public void processDltMessage(ConsumerRecord<String,String> message, Acknowledgment ack) {
        log.info("Processing record from DLT with key {} and value {}", message.key(), message.value());
        deadLetterService.save(message.key(), message.value(), message.topic());
        ack.acknowledge();
    }
}
