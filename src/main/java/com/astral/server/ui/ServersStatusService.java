package com.astral.server.ui;

import com.astral.server.Main;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public final class ServersStatusService {

    private static ScheduledFuture<?> task;
    private static final JavaPlugin plugin = Main.getPlugin();
    private static final Map<UUID, ServerMenu> menus = new ConcurrentHashMap<>();

    public static void addMenu(UUID uuid, ServerMenu menu) {
        menus.put(uuid, menu);
    }

    public static void removeMenu(UUID uuid) {
        menus.remove(uuid);
    }

    public static void removeMenu(ServerMenu menu) {
        menus.values().remove(menu);
    }

    public static void start() {
        if (task != null && !task.isCancelled() && !menus.isEmpty()) {
            return;
        }

        task = HytaleServer.SCHEDULED_EXECUTOR.scheduleAtFixedRate(
                ServersStatusService::update,
                0,
                5,
                TimeUnit.SECONDS
        );
    }

    private static void update() {

    }

    public static void stop() {
        if (task != null) {
            task.cancel(false);
            task = null;
        }
    }
}