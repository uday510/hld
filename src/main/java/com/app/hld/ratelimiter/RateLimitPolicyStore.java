package com.app.hld.ratelimiter;

import java.util.HashMap;
import java.util.Map;

public class RateLimitPolicyStore {

    private final Map<String, RateLimitRule> rules = new HashMap<>();

    private final int SIXTY_THOUSAND_MILLIS = 60_000;

    public RateLimitPolicyStore() {
        rules.put("/login", new RateLimitRule(SIXTY_THOUSAND_MILLIS, 5));
        rules.put("/search", new RateLimitRule(SIXTY_THOUSAND_MILLIS, 1000));
        rules.put("/checkout", new RateLimitRule(SIXTY_THOUSAND_MILLIS, 1));
    }

    public RateLimitRule getRule(String api) {
        return rules.getOrDefault(
                api,
                new RateLimitRule(SIXTY_THOUSAND_MILLIS, 100)
        );
    }

}
