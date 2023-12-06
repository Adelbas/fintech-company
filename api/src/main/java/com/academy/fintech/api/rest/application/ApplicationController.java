package com.academy.fintech.api.rest.application;

import com.academy.fintech.api.core.application.ApplicationService;
import com.academy.fintech.api.rest.dto.ApplicationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Application rest controller that provides application creation
 * Uses {@link ApplicationService} to perform logic.
 * Uses {@link ApplicationMapper} to map request to DTO.
 */
@Slf4j
@RestController
@RequestMapping("/api/application")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    private final ApplicationMapper applicationMapper;

    @PostMapping
    public String create(@RequestBody ApplicationRequest applicationRequest) {
        return applicationService.createApplication(applicationMapper.toApplicationDto(applicationRequest));
    }

}
