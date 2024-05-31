package com.academy.fintech.dwh.core.service.kafka.db;

import com.academy.fintech.dwh.core.service.kafka.db.entity.DeadLetter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeadLetterRepository extends JpaRepository<DeadLetter, Long> {
}
