package com.academy.fintech.mp.public_interface.payment.dto;

import com.academy.fintech.mp.core.service.payment.db.entity.enums.PaymentStatus;
import jakarta.annotation.Nullable;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record StatusCheckResponseDto (
        PaymentStatus status,
        LocalDateTime completionDate
){ }
