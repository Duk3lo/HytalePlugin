package com.astral.server.ui;

import com.astral.server.Main;
import com.astral.server.redis.RedisService;
import com.astral.server.redis.RedisSocketClient;
import com.hypixel.hytale.logger.HytaleLogger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.io.IOException;
import java.time.Clock;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class RedisMenuCache {

    private static final long DEFAULT_TTL_MS = 5_000L;
    private static final RedisMenuCache INSTANCE = new RedisMenuCache(DEFAULT_TTL_MS);

    public static RedisMenuCache getInstance() {
        return INSTANCE;
    }

    private final long ttlMs;
    private final Clock clock = Clock.systemUTC();

    private Map<String, String> cached = Collections.emptyMap();
    private long lastRefreshEpochMs = 0L;

    private final Object lock = new Object();

    private RedisMenuCache(long ttlMs) {
        this.ttlMs = ttlMs;
    }


    public @NotNull @UnmodifiableView Map<String, String> getCounts() {
        long now = clock.millis();
        boolean shouldRefresh;
        synchronized (lock) {
            shouldRefresh = (now - lastRefreshEpochMs) >= ttlMs;
        }

        if (shouldRefresh && !ServersStatusService.getMenus().isEmpty()) {
            refreshFromRedis();
        }

        // devolvemos copia inmutable para evitar concurrencia externa
        synchronized (lock) {
            return Map.copyOf(cached);
        }
    }


    public void forceRefreshIfMenus() {
        if (!ServersStatusService.getMenus().isEmpty()) {
            refreshFromRedis();
        }
    }

    private void refreshFromRedis() {
        synchronized (lock) {
            long now = clock.millis();
            if ((now - lastRefreshEpochMs) < ttlMs) return;
            lastRefreshEpochMs = now;
        }

        try {
            RedisService redisService = Main.getInstance().getRedisService();
            if (redisService == null || !redisService.isAvailable()) {
                HytaleLogger.getLogger().atInfo().log("RedisMenuCache: Redis no disponible, manteniendo cache actual");
                return;
            }

            RedisSocketClient client = redisService.getClient();
            if (client == null) {
                HytaleLogger.getLogger().atInfo().log("RedisMenuCache: Redis client es null, manteniendo cache actual");
                return;
            }

            Map<String, String> newMap;
            try {
                newMap = client.hgetAll("servers:players");
            } catch (IOException ioe) {
                HytaleLogger.getLogger().atWarning().log("RedisMenuCache: fallo al leer Redis: " + ioe.getMessage());
                synchronized (lock) {
                    lastRefreshEpochMs = 0L;
                }
                return;
            }

            synchronized (lock) {
                cached = new HashMap<>(newMap);
                lastRefreshEpochMs = clock.millis();
            }

            HytaleLogger.getLogger().atInfo().log("RedisMenuCache: cache actualizado (" + cached.size() + " entries)");
        } catch (Exception e) {
            HytaleLogger.getLogger().atWarning().log("RedisMenuCache: excepción inesperada: " + e.getMessage());
            synchronized (lock) {
                lastRefreshEpochMs = 0L;
            }
        }
    }
}
