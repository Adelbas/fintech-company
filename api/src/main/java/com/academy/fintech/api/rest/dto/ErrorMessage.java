package com.academy.fintech.api.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.util.Date;
import java.util.Map;

/**
 * Error message that returns if exception is thrown
 *
 * @param status exception status
 * @param timestamp exception occurrence time
 * @param message message
 * @param details map of exception details
 */
@Builder
public record ErrorMessage (
       int status,
       Date timestamp,
       String message,
       @JsonInclude(JsonInclude.Include.NON_NULL)
       Map<String,String> details
){ }
