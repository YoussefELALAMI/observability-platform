package com.observability.platform.api.dto.request;

import com.observability.platform.domain.model.LogLevel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventRequest {

    @NotBlank
    @Size(max = 100)
    @Pattern(regexp = "^[a-zA-Z0-9/_-]+$", message = "service must be alphanumeric with - / _" )
    private String service;

    @NotNull
    @PastOrPresent
    private Instant timestamp;

    @NotNull
    private LogLevel level;

    @Size(max = 5000)
    private String message;

    @Valid
    @NotNull
    private MetadataRequest metadata;
}
