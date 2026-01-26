package com.app.hld.ratelimiter;

public class RateLimitRule {

    private final long windowsSizeMillis;
    private final long maxRequests;

    public RateLimitRule(long windowsSizeMillis, long maxRequests) {
        this.windowsSizeMillis = windowsSizeMillis;
        this.maxRequests = maxRequests;
    }

    public long getWindowsSizeMillis() {
        return windowsSizeMillis;
    }

    public long getMaxRequests() {
        return maxRequests;
    }
}
