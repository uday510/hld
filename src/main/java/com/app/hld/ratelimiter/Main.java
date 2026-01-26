package com.app.hld.ratelimiter;

public class Main {

    public static void main(String[] args) {
        RedisClientProvider redisClientProvider =
                new RedisClientProvider("redis://localhost:6379");

        FixedWindowCounter counter =
                new FixedWindowCounter(redisClientProvider.redis());

        RateLimitPolicyStore policyStore =
                new RateLimitPolicyStore();

        String userId = "user123";
        String api = "/checkout";

        RateLimitRule rule = policyStore.getRule(api);

        String rateLimitKey = userId + ":" + api;

        for (int i = 1; i <= 3; i++) {
            boolean allowed = counter.allow(rateLimitKey, rule);

            System.out.println(
                    "Request " + i + " -> " +
                            (allowed ? "ALLOWED" : "RATE_LIMIT_EXCEEDED")
            );
        }

    }


}
