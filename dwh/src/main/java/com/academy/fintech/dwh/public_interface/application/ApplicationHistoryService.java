package com.academy.fintech.dwh.public_interface.application;

import com.academy.fintech.dwh.public_interface.application.dto.ApplicationStatusUpdateDto;

public interface ApplicationHistoryService {
    void handleApplicationStatusUpdate(ApplicationStatusUpdateDto applicationStatusUpdateDto);
}
