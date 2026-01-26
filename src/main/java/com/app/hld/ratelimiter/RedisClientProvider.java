package com.app.hld.ratelimiter;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

public class RedisClientProvider {

    private final RedisCommands<String, String> redis;

    public RedisClientProvider(String uri) {
        RedisClient client = RedisClient.create(uri);
        StatefulRedisConnection<String, String> connection = client.connect();
        redis = connection.sync();
    }

    public RedisCommands<String, String> redis() {
        return redis;
    }
}
