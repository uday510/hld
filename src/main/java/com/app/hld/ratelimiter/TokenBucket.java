package com.app.hld.ratelimiter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class TokenBucket {

    private final double capacity;
    private final double refillRatePerSecond;
    private final Map<String, Bucket> buckets;

    public TokenBucket(double capacity, double refillRatePerSecond) {
        this.capacity = capacity;
        this.refillRatePerSecond = refillRatePerSecond;
        this.buckets = new ConcurrentHashMap<>();
    }

    public boolean allowRequest(String userId, String ipAddress) {
        String key = userId + ":" + ipAddress;
        AtomicBoolean allowed = new AtomicBoolean(false);

        buckets.compute(key, (k, bucket) -> {
            long now = System.nanoTime();

            if (bucket == null) {
                bucket = new Bucket(capacity, now);
            } else {
                double elapsedSeconds = (now - bucket.lastRefillNanos) / 1e9;
                if (elapsedSeconds > 0) {
                    bucket.tokens = Math.min(capacity, bucket.tokens + (elapsedSeconds * refillRatePerSecond));
                    bucket.lastRefillNanos = now;
                }
            }

            if (bucket.tokens >= 1.0) {
                bucket.tokens -= 1.0;
                allowed.set(true);
            }

            return bucket;
        });


        return allowed.get();
    }


    private static class Bucket {
        double tokens;
        long lastRefillNanos;

        Bucket(double tokens, long lastRefillNanos) {
            this.tokens = tokens;
            this.lastRefillNanos = lastRefillNanos;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // 10 requests per minute -> capacity = 10, refillRate = 10 / 60.0 (1 token every 6s)
        TokenBucket limiter = new TokenBucket(10, 10.0 / 60.0);

        String user = "user_1";
        String ip = "192.168.1.10";

        for (int i = 1; i <= 12; i++) {
            boolean allowed = limiter.allowRequest(user, ip);
            System.out.printf("Request %2d: %s%n", i, allowed ? "200 OK (Allowed)" : "429 Too Many Requests");
        }

        System.out.println("\nWaiting 6 seconds for 1 token to refill...");
        Thread.sleep(6000);

        System.out.println("Request 13: " + (limiter.allowRequest(user, ip) ? "200 OK (Allowed)" : "429 Too Many Requests"));    }
}
