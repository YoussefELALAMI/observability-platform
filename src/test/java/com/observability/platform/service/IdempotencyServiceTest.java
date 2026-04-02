package com.observability.platform.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IdempotencyServiceTest {

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @InjectMocks
    private IdempotencyService idempotencyService;

    @Test
    void isAlreadyProcessed_keyExists_returnsTrue() {
        // Arrange
        when(redisTemplate.hasKey("idem:req-123")).thenReturn(true);

        // Act
        boolean result = idempotencyService.isAlreadyProcessed("req-123");

        // Assert
        assertThat(result).isTrue();
    }

    @Test
    void isAlreadyProcessed_keyNotExists_returnsFalse() {
        // Arrange
        when(redisTemplate.hasKey("idem:req-124")).thenReturn(false);

        boolean result = idempotencyService.isAlreadyProcessed("req-124");

        assertThat(result).isFalse();
    }

    @Test
    void markAsProcessed_storesCorrectKeyWithTTL() {

        // Arrange
        // opsForValue() is the Redis method that gives access to simple key-value operations.
        // Since redisTemplate is a mock, opsForValue() returns null by default.
        // So we create a mock for ValueOperations and tell redisTemplate to return it.
        ValueOperations<String, String> ops = mock(ValueOperations.class);
        when(redisTemplate.opsForValue()).thenReturn(ops);

        // Act
        // Call the real method we are testing.
        // Internally this calls: redisTemplate.opsForValue().set("idem:req-125", "processed", 1, HOURS)
        idempotencyService.markAsProcessed("req-125");

        // Assert
        // We don't assert a return value here because markAsProcessed() returns void.
        // Instead we VERIFY that set() was called on ops with exactly these arguments:
        // - key   → "idem:req-125"  (correct prefix + requestId)
        // - value → "processed"     (the placeholder value)
        // - ttl   → 1 HOUR          (expires after 1 hour)
        // If any argument is wrong, this line fails.
        verify(ops).set("idem:req-125", "processed", 1, TimeUnit.HOURS);
    }
}
