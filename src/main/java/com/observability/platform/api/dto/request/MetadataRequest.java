package com.observability.platform.api.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
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