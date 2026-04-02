package com.observability.platform.api.dto.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class MetadataRequest {

    @NotBlank
    @Size(max = 100)
    private String requestId;

    @Positive
    private String latencyMs;

    @DecimalMin("0.0")
    @DecimalMax("1.0")
    private BigDecimal cpuUsage;
}