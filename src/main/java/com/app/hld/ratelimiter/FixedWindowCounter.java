package com.app.hld.ratelimiter;

import io.lettuce.core.ScriptOutputType;
import io.lettuce.core.api.sync.RedisCommands;

public class FixedWindowCounter {

    private static final String LUA_SCRIPT =
                    "local current = redis.call('INCR', KEYS[1]) " +
                    "if current == 1 then " +
                    "   redis.call('PEXPIRE', KEYS[1], ARGV[2]) " +
                    "end " +
                     "if current > tonumber(ARGV[1]) then " +
                     "  return 0 " +
                     "end " +
                     "return 1";


    private final RedisCommands<String, String> redis;


    public FixedWindowCounter(RedisCommands<String, String> redis) {
        this.redis = redis;
    }

    public boolean allow(
            String key,
            RateLimitRule rule
    ) {
        long now = System.currentTimeMillis();
        long windowStart = now - (now % rule.getWindowsSizeMillis());

        String redisKey = buildRedisKey(key, windowStart);

        Long result = redis.eval(
          LUA_SCRIPT,
          ScriptOutputType.INTEGER,
          new String[]{redisKey},
          String.valueOf(rule.getMaxRequests()),
          String.valueOf(rule.getWindowsSizeMillis())
        );

        return result != null && result == 1;
    }

    private String buildRedisKey(String key, long windowStart) {
        return "rate_limit:" + key + ":" + windowStart;
    }
}
