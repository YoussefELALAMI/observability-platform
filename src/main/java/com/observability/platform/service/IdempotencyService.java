package com.observability.platform.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Idempotency service stores and checks keys in Redis to ensure that the same request is not processed multiple times.
 * The keys are stored with a prefix "idem:" followed by the request ID, and they expire after 1 hour to prevent indefinite growth of Redis data.
 * This service is used by the IngestionService to check if a request has already been processed and to mark it as processed after handling the event.
 */
@Service
@RequiredArgsConstructor
public class IdempotencyService {

    private final RedisTemplate<String, String> redisTemplate;

    public boolean isAlreadyProcessed(String requestId){
        return redisTemplate.hasKey("idem:" + requestId);
    }

    public void markAsProcessed(String requestId){
        redisTemplate.opsForValue().set("idem:" + requestId, "processed", 1, TimeUnit.HOURS);
    }
}