package com.observability.platform.domain.entity;

import com.observability.platform.domain.model.LogLevel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name="logs", schema = "observability_schema", indexes = {
        @Index(name = "idx_logs_timestamp", columnList = "timestamp DESC"),
        @Index(name = "idx_logs_request_id", columnList = "request_id"),
        @Index(name = "idx_logs_service_timestamp", columnList = "service, timestamp DESC"),
        @Index(name = "idx_logs_level_timestamp", columnList = "level, timestamp DESC")
})
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(name = "service", nullable = false, length = 100)
    String service;

    @Column(name = "timestamp", nullable = false)
    OffsetDateTime timestamp;

    @Enumerated(EnumType.STRING)
    @Column(name = "level", nullable = false, length = 20)
    LogLevel level;

    @Column(name = "message")
    String message;

    @Column(name = "request_id", unique = true, length = 100)
    String requestId;

    @Column(name = "latency_ms")
    Integer latencyMs;

    @Column(name = "cpu_usage")
    BigDecimal cpuUsage;

    @Column(name = "endpoint",  length = 255)
    String endpoint;

    @Column(name = "created_at", insertable = false, updatable = false)
    OffsetDateTime createdAt;
}
