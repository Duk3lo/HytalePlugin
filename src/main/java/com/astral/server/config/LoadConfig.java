package com.astral.server.config;

import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.util.Config;

import java.nio.file.Path;

public final class LoadConfig {

    public static void build(JavaPlugin plugin, Config<PluginConfig> config) {
        try {
            Path target = plugin.getDataDirectory().resolve("plugin-config.json");

            if (!java.nio.file.Files.exists(target)) {
                config.load()
                        .thenCompose(_ -> config.save())
                        .join();

                plugin.getLogger().atInfo()
                        .log("plugin-config.json creado en: %s", target.toString());
            } else {
                config.load().join();

                plugin.getLogger().atInfo()
                        .log("plugin-config.json cargado desde: %s", target.toString());
            }

        } catch (Exception e) {
            plugin.getLogger().atSevere()
                    .withCause(e)
                    .log("Error al asegurar plugin-config.json");
        }
    }
}
