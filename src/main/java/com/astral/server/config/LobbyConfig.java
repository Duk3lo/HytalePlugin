package com.astral.server.config;

public final class LobbyConfig {

    public final String mode;
    public final String redisId;
    public final String displayName;

    public LobbyConfig(String mode, String redisId, String displayName) {
        this.mode = mode;
        this.redisId = redisId;
        this.displayName = displayName;
    }
}
