package com.astral.server.config;

import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.util.Config;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public final class LoadConfig {

    public static void build(JavaPlugin plugin, @NotNull Map<String, Config<?>> configuration) {
        for (Map.Entry<String, Config<?>> entry : configuration.entrySet()) {
            String key = entry.getKey();
            String fullKey = key+".json";
            Config<?> config = entry.getValue();
            try {
                Path target = plugin.getDataDirectory().resolve(fullKey);
                if (!Files.exists(target)) {
                    config.load()
                            .thenCompose(_ -> config.save())
                            .join();
                    plugin.getLogger().atInfo()
                            .log(fullKey + " creado en: %s", target.toString());
                } else {
                    config.load().join();
                    plugin.getLogger().atInfo()
                            .log(fullKey + " cargado desde: %s", target.toString());
                }
            } catch (Exception e) {
                plugin.getLogger().atSevere()
                        .withCause(e)
                        .log("Error al asegurar: " + fullKey);
            }
        }
    }
}
