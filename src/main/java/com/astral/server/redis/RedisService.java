package com.astral.server.redis;

import com.hypixel.hytale.logger.HytaleLogger;

import java.io.IOException;

public final class RedisService {

    private final String host;
    private final int port;
    private final int timeout;
    private final String password;
    private final HytaleLogger logger;
    private RedisSocketClient client;

    public RedisService(String host, int port, int timeout, String password, HytaleLogger logger) {
        this.host = host;
        this.port = port;
        this.timeout = timeout;
        this.password = password;
        this.logger = logger;
        reconnect();
    }
    public synchronized RedisSocketClient getClient() {
        try {
            if (client == null || !client.isAlive(timeout)) {
                reconnect();
            }
        } catch (Exception e) {
            logger.atWarning().log("Redis reconnection failed: " + e.getMessage());
        }
        return client;
    }

    private synchronized void reconnect() {
        try {
            if (client != null) {
                try { client.close(); } catch (IOException ignored) {}
            }

            client = new RedisSocketClient(host, port, timeout);

            if (password != null && !password.isBlank()) {
                logger.atInfo().log("Authenticating with Redis...");
                boolean ok = client.auth(password);
                if (!ok) {
                    throw new IOException("AUTH failed");
                }
            } else {
                logger.atInfo().log("Redis AUTH skipped (no password)");
            }

            logger.atInfo().log("Redis connected -> " + host + ":" + port);
        } catch (IOException e) {
            logger.atInfo().log("Unable to connect to Redis " + host + ":" + port + " -> " + e.getMessage(), e);
            client = null;
        }
    }

    public boolean isAvailable() {
        return client != null && client.isAlive();
    }
}
