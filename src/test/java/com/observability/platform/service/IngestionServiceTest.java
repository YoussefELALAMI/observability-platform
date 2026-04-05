package com.observability.platform.service;

import com.observability.platform.api.dto.request.EventRequest;
import com.observability.platform.api.dto.request.MetadataRequest;
import com.observability.platform.domain.model.LogLevel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IngestionServiceTest {

    @Mock
    private IdempotencyService idempotencyService;

    @InjectMocks
    IngestionService ingestionService;

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

    // --- requestId already processed ?
    @Test
    void ingest_duplicateRequest_skipsProcessing(){
        // Arrange
        EventRequest request = validRequest();
        doReturn(true).when(idempotencyService).isAlreadyProcessed("req-abc-123");

        // Act
        ingestionService.ingest(request);

        // Assert
        verify(idempotencyService, never()).markAsProcessed(any()); // if the request is a duplicate, markAsProcessed should never be called
    }

    // --- requestId is new
    @Test
    void ingest_newRequest_marksAsProcessed(){
        // Arrange
        EventRequest request = validRequest();
        doReturn(false).when(idempotencyService).isAlreadyProcessed("req-abc-123");

        // Act
        ingestionService.ingest(request);

        // Assert
        verify(idempotencyService).markAsProcessed("req-abc-123"); // if the request is new, markAsProcessed should be called with the correct requestId
    }
}
