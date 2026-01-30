/*
package com.astral.server.config;

import com.hypixel.hytale.server.core.plugin.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public final class ServerConfig {

    private static final Map<String, LobbyConfig> LOBBIES = new HashMap<>();

    public static void load(JavaPlugin plugin) {
        plugin.saveResource("config/servers.yml", false);

        File file = new File(plugin.getDataFolder(), "config/servers.yml");
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);

        ConfigurationSection modes =
                cfg.getConfigurationSection("menus.main.modes");

        if (modes == null) return;

        for (String key : modes.getKeys(false)) {
            String redisId = modes.getString(key + ".redis-id");
            String display = modes.getString(key + ".display-name");

            LOBBIES.put(key, new LobbyConfig(key, redisId, display));
        }
    }

    public static Collection<LobbyConfig> getLobbies() {
        return LOBBIES.values();
    }
}
*/