package com.observability.platform.repository;

import com.observability.platform.domain.entity.Log;
import com.observability.platform.domain.model.LogLevel;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class LogRepositoryTest {

    @Autowired
    private LogRepository logRepository;

    @BeforeEach
    public void setUp() {
        logRepository.deleteAll();
    }

    // ── Helper method to avoid repeating Log construction ──────────────────
    private Log buildLog(String serviceName, LogLevel level, String message) {
        Log log = new Log();
        log.setService(serviceName);
        log.setLevel(level);
        log.setMessage(message);
        log.setTimestamp(OffsetDateTime.now());
        return log;
    }

    // ── Entity tests ────────────────────────────────────────────────────────
    @Test
    @DisplayName("Should save a log entry and assign it an ID")
    void testSaveLog() {
        // ARRANGE - prepare the data
        Log log = buildLog("payment-service", LogLevel.INFO, "Payment processed successfully");

        // ACT - perform the action
        Log savedLog = logRepository.save(log);

        // ASSERT - verify the results
        Assertions.assertNotNull(savedLog.getId(), "Saved log should have an ID assigned");
        Assertions.assertEquals("payment-service", savedLog.getService(), "Service name should match");
        Assertions.assertEquals(LogLevel.INFO, savedLog.getLevel(), "Log level should match");
        Assertions.assertEquals("Payment processed successfully", savedLog.getMessage(), "Log message should match");
    }

    @Test
    @DisplayName("Should persist all fields correctly")
    void shouldPersistAllFields() {
        Log log = buildLog("auth-service", LogLevel.ERROR, "Token expired");

        Log saved = logRepository.save(log);
        // Flush and clear forces Hibernate to hit the DB,
        // not just return the cached object
        logRepository.flush();

        Optional<Log> found = logRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getService()).isEqualTo("auth-service");
        assertThat(found.get().getLevel()).isEqualTo(LogLevel.ERROR);
        assertThat(found.get().getMessage()).isEqualTo("Token expired");
    }

    @Test
    @DisplayName("Should return empty Optional when ID does not exist")
    void shouldReturnEmptyForUnknownId() {
        Optional<Log> found = logRepository.findById(999L);

        assertThat(found).isEmpty();
    }

    // ── Query method tests ──────────────────────────────────────────────────

    @Test
    @DisplayName("findByLogLevel should return only logs with matching level")
    void shouldFindByLogLevel() {
        // ARRANGE — save a mix of levels
        logRepository.save(buildLog("service-a", LogLevel.ERROR, "Error one"));
        logRepository.save(buildLog("service-b", LogLevel.ERROR, "Error two"));
        logRepository.save(buildLog("service-c", LogLevel.INFO,  "Info message"));

        // ACT
        List<Log> errors = logRepository.findByLevel(LogLevel.ERROR);

        // ASSERT
        assertThat(errors).hasSize(2);
        assertThat(errors).allMatch(l -> l.getLevel() == LogLevel.ERROR);
    }

    @Test
    @DisplayName("findByServiceName should return only logs from that service")
    void shouldFindByServiceName() {
        logRepository.save(buildLog("payment-service", LogLevel.INFO, "Payment OK"));
        logRepository.save(buildLog("payment-service", LogLevel.ERROR, "Payment failed"));
        logRepository.save(buildLog("auth-service",    LogLevel.INFO, "Login OK"));

        List<Log> paymentLogs = logRepository.findByService("payment-service");

        assertThat(paymentLogs).hasSize(2);
        assertThat(paymentLogs).allMatch(l -> l.getService().equals("payment-service"));
    }

    @Test
    @DisplayName("findByServiceNameAndLogLevel should filter by both fields")
    void shouldFindByServiceNameAndLogLevel() {
        logRepository.save(buildLog("payment-service", LogLevel.ERROR, "Payment failed"));
        logRepository.save(buildLog("payment-service", LogLevel.INFO,  "Payment OK"));
        logRepository.save(buildLog("auth-service",    LogLevel.ERROR, "Auth failed"));

        List<Log> result = logRepository.findByServiceAndLevel(
                "payment-service", LogLevel.ERROR);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getMessage()).isEqualTo("Payment failed");
    }

    @Test
    @DisplayName("findByLogLevel should return empty list when no matches")
    void shouldReturnEmptyListWhenNoMatches() {
        logRepository.save(buildLog("service-a", LogLevel.INFO, "Info only"));

        List<Log> fatals = logRepository.findByLevel(LogLevel.WARN);

        assertThat(fatals).isEmpty();
    }
}
