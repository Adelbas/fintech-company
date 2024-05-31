package com.academy.fintech.dwh.core.service.kafka;

import com.academy.fintech.dwh.core.service.kafka.db.DeadLetterRepository;
import com.academy.fintech.dwh.core.service.kafka.db.entity.DeadLetter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DeadLetterService {

    private final DeadLetterRepository deadLetterRepository;

    public void save(String key, String value, String topic) {
        deadLetterRepository.save(DeadLetter.builder()
                .key(key)
                .value(value)
                .topic(topic)
                .receivedAt(LocalDateTime.now())
                .build()
        );
    }
}
