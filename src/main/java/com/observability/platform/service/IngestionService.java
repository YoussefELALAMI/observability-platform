package com.observability.platform.service;

import com.observability.platform.api.dto.request.EventRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * IngestionService is responsible for processing incoming events.
 * The processing flow includes:
 * 1. Check idempotency    → calls IdempotencyService
 * 2. Publish to Kafka     → calls KafkaProducer
 * 3. Mark as processed    → calls IdempotencyService again
 */
@Service
@RequiredArgsConstructor
public class IngestionService {

    private final IdempotencyService idempotencyService;

    public void ingest(EventRequest eventRequest){

        String requestId = eventRequest.getMetadata().getRequestId();

        if(idempotencyService.isAlreadyProcessed(requestId)) return; // if the request has already been processed, we simply return and do nothing

        // TODO: publish to Kafka

        idempotencyService.markAsProcessed(requestId); // mark the request as processed in Redis to prevent future duplicate processing
    }
}
