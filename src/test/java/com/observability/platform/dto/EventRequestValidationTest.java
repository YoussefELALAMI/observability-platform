package com.observability.platform.dto;

import com.observability.platform.api.dto.request.EventRequest;
import com.observability.platform.api.dto.request.MetadataRequest;
import com.observability.platform.domain.model.LogLevel;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class EventRequestValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    // --- Helper method to create a valid EventRequest for testing ---

    private EventRequest validRequest() {
        EventRequest req = new EventRequest();
        req.setService("payment-service");
        req.setTimestamp(Instant.now());
        req.setLevel(LogLevel.ERROR);
        req.setMessage("Something failed");

        MetadataRequest meta = new MetadataRequest();
        meta.setRequestId("req-abc-123");
        req.setMetadata(meta);

        return req;
    }

    // --- service field ---

    @Test
    void service_blank_shouldFailValidation() {
        // Arrange
        EventRequest request = validRequest();
        request.setService("");

        // Act
        var violations = validator.validate(request);

        // Assert
        assertThat(violations)
                .anyMatch(v -> v.getPropertyPath().toString().equals("service"));
    }

    @Test
    void service_validValue_shouldPass() {
        // Arrange
        EventRequest request = validRequest();

        // Act
        var violations = validator.validate(request);

        // Assert
        assertThat(violations).isEmpty();
    }

    @Test
    void service_invalidCharacters_shouldFailValidation() {
        // Arrange
        EventRequest request = validRequest();
        request.setService("invalid service!"); // contains space and exclamation mark

        // Act
        var violations = validator.validate(request);

        // Assert
        assertThat(violations)
                .anyMatch(v -> v.getPropertyPath().toString().equals("service"));
    }

    // --- timestamp field ---

    @Test
    void timestamp_null_shouldFailValidation() {
        // Arrange
        EventRequest request = validRequest();
        request.setTimestamp(null);

        // Act
        var violations = validator.validate(request);

        // Assert
        assertThat(violations)
                .anyMatch(v -> v.getPropertyPath().toString().equals("timestamp"));
    }

    @Test
    void timestamp_inTheFuture_shouldFailValidation() {
        // Arrange
        EventRequest request = validRequest();
        request.setTimestamp(Instant.now().plusSeconds(3600)); // 1 hour in the future

        // Act
        var violations = validator.validate(request);

        // Assert
        assertThat(violations)
                .anyMatch(v -> v.getPropertyPath().toString().equals("timestamp"));
    }

    // --- level field ---

    @Test
    void level_null_shouldFailValidation() {
        EventRequest request = validRequest();
        request.setLevel(null);

        var violations = validator.validate(request);

        assertThat(violations)
                .anyMatch(v -> v.getPropertyPath().toString().equals("level"));
    }

    // --- message field ---

    @Test
    void message_exceedsLength_shouldFailValidation() {
        EventRequest request = validRequest();
        request.setMessage("Something failed".repeat(500)); // 500 * 17 = 8500 chars, exceeds 5000 limit
    }

    // --- metadata field ---

    @Test
    void metadata_requestId_blank_shouldFailValidation() {
        // Arrange
        EventRequest request = validRequest();
        request.getMetadata().setRequestId("");

        // Act
        var violations = validator.validate(request);

        // Assert
        assertThat(violations)
                .anyMatch(v -> v.getPropertyPath().toString()
                        .equals("metadata.requestId"));  // note the dot path
    }
}
