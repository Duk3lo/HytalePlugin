package com.astral.server.events.task;

import com.astral.server.util.SpawnTeleporter;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.jetbrains.annotations.NotNull;
import java.util.UUID;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class StatusPosition implements Runnable {

    private final double minY;
    private final double maxY;

    public StatusPosition(double minY, double maxY) {
        this.minY = Math.min(minY, maxY);
        this.maxY = Math.max(minY, maxY);
    }

    @Override
    public void run() {
        for (PlayerRef playerRef : Universe.get().getPlayers()) {
            if (playerRef == null) continue;
            UUID worldUUID = playerRef.getWorldUuid();
            if (worldUUID == null) continue;
            World world = Universe.get().getWorld(worldUUID);
            if (world == null) continue;
            Vector3d position = playerRef.getTransform().getPosition();
            double y = position.getY();
            if (y < minY || y > maxY) {
                Ref<EntityStore> ref = playerRef.getReference();
                if (ref == null) {return;}
                SpawnTeleporter.teleportToSpawn(playerRef, world ,true);
            }
        }
    }

    public void schedule(@NotNull ScheduledExecutorService executor) {
        executor.scheduleAtFixedRate(this, 500L, 500L, TimeUnit.MILLISECONDS);
    }
}