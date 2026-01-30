package com.astral.server.ui;

import com.hypixel.hytale.server.core.HytaleServer;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public final class ServersStatusService {

    private static ScheduledFuture<?> task;
    private static final Map<UUID, ServerMenu> menus = new ConcurrentHashMap<>();
    private static final AtomicInteger menuCounter = new AtomicInteger(0);

    private ServersStatusService() {
    }

    public static synchronized void addMenu(UUID uuid, ServerMenu menu) {
        menus.put(uuid, menu);

        if (menus.size() == 1) {
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

        task = HytaleServer.SCHEDULED_EXECUTOR.scheduleAtFixedRate(
                ServersStatusService::update,
                0,
                5,
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
        if (menus.isEmpty()) {
            return;
        }
        System.out.println("task");
        for (ServerMenu menu : menus.values()) {
            menu.updateMode("Vanilla", "Prueba esto",String.valueOf(menuCounter.incrementAndGet()));
        }
    }
}
