package com.astral.server.ui;

import com.astral.server.Main;
import com.astral.server.config.PluginConfig;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.HytaleServer;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


public final class ServersStatusService {

    private static ScheduledFuture<?> task;
    private static final Map<UUID, ServerMenu> menus = new ConcurrentHashMap<>();
    private static final Main plugin = Main.getInstance();

    private ServersStatusService() {
    }

    public static Map<UUID, ServerMenu> getMenus() {
        return menus;
    }

    public static synchronized void addMenu(UUID uuid, ServerMenu menu) {
        menus.put(uuid, menu);

        if (menus.size() == 1) {
            RedisMenuCache.getInstance().forceRefreshIfMenus();
            start();
        }
    }

    public static synchronized void removeMenu(UUID uuid) {
        menus.remove(uuid);

        if (menus.isEmpty()) {
            stop();
        }
    }

    private static synchronized void start() {
        if (task != null && !task.isCancelled()) {
            return;
        }

        long periodSeconds;
        try {
            periodSeconds = plugin.getPluginConfig()
                    .getMenuLobby()
                    .getRedis()
                    .getUpdateUI();
            if (periodSeconds <= 0) periodSeconds = 5;
        } catch (Exception e) {
            periodSeconds = 5;
        }

        task = HytaleServer.SCHEDULED_EXECUTOR.scheduleAtFixedRate(
                ServersStatusService::update,
                1,
                periodSeconds,
                TimeUnit.SECONDS
        );
    }

    private static synchronized void stop() {
        if (task != null) {
            task.cancel(false);
            task = null;
        }
    }

    private static void update() {
        if (menus.isEmpty()) return;
        PluginConfig.Redis redisCfg = plugin.getPluginConfig().getMenuLobby().getRedis();
        final int max = 100;

        for (ServerMenu menu : menus.values()) {
            if (!redisCfg.isEnabled()) {
                for (String mode : menu.getModes()) {
                    menu.updateMode(mode, false, 0, max);
                }
                continue;
            }

            try {
                Map<String, String> counts = RedisMenuCache.getInstance().getCounts();

                for (String mode : menu.getModes()) {
                    boolean online = counts.containsKey(mode);
                    int players = 0;
                    if (online) {
                        String val = counts.get(mode);
                        if (val != null) {
                            try {
                                players = Integer.parseInt(val);
                            } catch (NumberFormatException ignored) {
                            }
                        }
                    }
                    menu.updateMode(mode, online, players, max);
                }

            } catch (Exception e) {
                HytaleLogger.getLogger().atWarning().log("Error en ServersStatusService.update: " + e.getMessage());
                for (String mode : menu.getModes()) {
                    menu.updateMode(mode, false, 0, max);
                }
            }
        }
    }

}