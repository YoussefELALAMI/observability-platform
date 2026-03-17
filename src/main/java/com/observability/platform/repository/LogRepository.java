package com.observability.platform.repository;

import com.observability.platform.domain.entity.Log;
import com.observability.platform.domain.model.LogLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface LogRepository extends JpaRepository<Log, Long> {

    // Find all logs by level
    List<Log> findByLevel(LogLevel logLevel);

    // Find all logs by a requestId
    List<Log> findByRequestId(Integer requestId);

    // Find all logs by service name
    List<Log> findByService(String service);

    // Find all logs from service name filtered by level
    List<Log> findByServiceAndLevel(String service, LogLevel logLevel);

    // Find logs within a specific time range
    List<Log> findByCreatedAtBetween(OffsetDateTime start, OffsetDateTime end);

    // Find recent ERROR/WARN logs for a specific service, newest first
    List<Log> findByServiceAndLevelOrderByCreatedAtDesc(String service, LogLevel logLevel);
}
