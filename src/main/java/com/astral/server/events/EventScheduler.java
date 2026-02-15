package com.astral.server.events;

import com.astral.server.events.task.StatusPosition;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

public final class EventScheduler {

    private final JavaPlugin plugin;
    private ScheduledExecutorService scheduler;
    private static final AtomicInteger THREAD_COUNTER = new AtomicInteger(1);

    public EventScheduler(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public synchronized void startStatusPosition(double minY, double maxY) {
        if (scheduler != null && !scheduler.isShutdown()) {
            plugin.getLogger().atInfo().log("EventScheduler: StatusPosition ya está en ejecución.");
            return;
        }
        this.scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r);
            t.setName("Astral-StatusPosition-Thread-" + THREAD_COUNTER.getAndIncrement());
            t.setDaemon(true);
            return t;
        });

        StatusPosition statusPosition = new StatusPosition(minY, maxY);
        statusPosition.schedule(this.scheduler);

        plugin.getLogger().atInfo().log("EventScheduler: StatusPosition programado (minY=" + minY + ", maxY=" + maxY + ").");
    }

    public synchronized void stop() {
        if (this.scheduler != null && !this.scheduler.isShutdown()) {
            this.scheduler.shutdownNow();
            this.scheduler = null;
            plugin.getLogger().atInfo().log("EventScheduler: scheduler detenido.");
        } else {
            plugin.getLogger().atInfo().log("EventScheduler: scheduler ya estaba detenido o no existe.");
        }
    }

}