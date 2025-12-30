package com.example.airesume.security;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class Bucket4jRateLimiter {

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    private final int capacity;
    private final Duration duration;

    public Bucket4jRateLimiter(
            @Value("${rate-limit.capacity}") int capacity,
            @Value("${rate-limit.duration}") Duration duration
    ) {
        this.capacity = capacity;
        this.duration = duration;
    }

    private Bucket createNewBucket() {
        Bandwidth limit = Bandwidth.classic(
                capacity,
                Refill.intervally(capacity, duration)
        );
        return Bucket.builder().addLimit(limit).build();
    }

    public boolean tryConsume(String key) {
        return buckets
                .computeIfAbsent(key, k -> createNewBucket())
                .tryConsume(1);
    }
}
