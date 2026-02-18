package com.astral.server.ui;

import com.astral.server.Main;
import com.astral.server.config.PluginConfig;
import com.astral.server.redis.RedisService;
import com.astral.server.redis.RedisSocketClient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.time.Clock;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class RedisMenuCache {

    // Fallback seguro: 5 segundos (en ms)
    private static final long FALLBACK_TTL_MS = 5_000L;

    private static final RedisMenuCache INSTANCE = new RedisMenuCache();

    public static RedisMenuCache getInstance() {
        return INSTANCE;
    }

    private final Clock clock = Clock.systemUTC();

    private Map<String, String> cached = Collections.emptyMap();
    private long lastRefreshEpochMs = 0L;

    private final Object lock = new Object();

    private RedisMenuCache() {
    }

    public @NotNull @UnmodifiableView Map<String, String> getCounts() {
        long now = clock.millis();
        long ttlMs = resolveTtlMs();

        boolean shouldRefresh;
        synchronized (lock) {
            shouldRefresh = (now - lastRefreshEpochMs) >= ttlMs;
        }

        if (shouldRefresh && !ServersStatusService.getMenus().isEmpty()) {
            refreshFromRedis(ttlMs);
        }

        synchronized (lock) {
            return Map.copyOf(cached);
        }
    }

    public void forceRefreshIfMenus() {
        if (!ServersStatusService.getMenus().isEmpty()) {
            refreshFromRedis(resolveTtlMs());
        }
    }

    private void refreshFromRedis(long ttlMs) {
        synchronized (lock) {
            long now = clock.millis();
            if ((now - lastRefreshEpochMs) < ttlMs) return;
            lastRefreshEpochMs = now;
        }

        try {
            RedisService redisService = Main.getInstance().getRedisService();
            if (redisService == null || !redisService.isAvailable()) {
                return;
            }

            RedisSocketClient client = redisService.getClient();
            if (client == null) {
                return;
            }

            Map<String, String> newMap = client.hgetAll("servers:players");

            synchronized (lock) {
                cached = new HashMap<>(newMap);
                lastRefreshEpochMs = clock.millis();
            }

        } catch (Exception e) {
            synchronized (lock) {
                lastRefreshEpochMs = 0L;
            }
        }
    }

    private long resolveTtlMs() {
        try {
            PluginConfig.Redis redisCfg =
                    Main.getInstance().getPluginConfig().getMenuLobby().getRedis();

            long seconds = redisCfg.getUpdateUI();
            if (seconds <= 0) return FALLBACK_TTL_MS;

            return seconds * 1000L;
        } catch (Exception e) {
            return FALLBACK_TTL_MS;
        }
    }
}
