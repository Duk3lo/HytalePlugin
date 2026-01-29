package com.astral.server.ui;

import com.hypixel.hytale.server.core.HytaleServer;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public final class ServersStatusService {

    private static ScheduledFuture<?> task;

    public static void start() {
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

    private static void update() {

    }

    public static void stop() {
        if (task != null) {
            task.cancel(false);
            task = null;
        }
    }
}