package com.astral.server.events.task;

import com.astral.server.Main;
import com.astral.server.config.PluginConfig;
import com.astral.server.util.SpawnTeleporter;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.math.vector.Vector3d;
import org.jetbrains.annotations.NotNull;
import java.util.UUID;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class StatusPosition implements Runnable {

    private double minY;
    private double maxY;
    private double minX;
    private double maxX;
    private double minZ;
    private double maxZ;

    @Override
    public void run() {
        PluginConfig config = Main.getInstance().getPluginConfig();
        PluginConfig.Limits limits = config.getLimits();
        boolean enabled = limits.isEnable();
        if (!enabled) {return;}
        double lMinY = limits.getMin_y();
        double lMaxY = limits.getMax_y();
        double lMinX = limits.getMin_x();
        double lMaxX = limits.getMax_x();
        double lMinZ = limits.getMin_z();
        double lMaxZ = limits.getMax_z();

        this.minY = Math.min(lMinY, lMaxY);
        this.maxY = Math.max(lMinY, lMaxY);
        this.minX = Math.min(lMinX, lMaxX);
        this.maxX = Math.max(lMinX, lMaxX);
        this.minZ = Math.min(lMinZ, lMaxZ);
        this.maxZ = Math.max(lMinZ, lMaxZ);

        for (PlayerRef playerRef : Universe.get().getPlayers()) {
            if (playerRef == null) continue;

            UUID worldUUID = playerRef.getWorldUuid();
            if (worldUUID == null) continue;

            World world = Universe.get().getWorld(worldUUID);
            if (world == null) continue;

            Vector3d position = playerRef.getTransform().getPosition();

            boolean outside = isABoolean(position);

            if (outside) {
                SpawnTeleporter.teleportToSpawn(playerRef, world, true);
            }
        }
    }

    private boolean isABoolean(@NotNull Vector3d position) {
        double x = position.getX();
        double y = position.getY();
        double z = position.getZ();
        boolean outside = false;
        if (!(minY == 0.0 && maxY == 0.0)) {
            if (y < minY || y > maxY) {
                outside = true;
            }
        }
        if (!(minX == 0.0 && maxX == 0.0)) {
            if (x < minX || x > maxX) {
                outside = true;
            }
        }
        if (!(minZ == 0.0 && maxZ == 0.0)) {
            if (z < minZ || z > maxZ) {
                outside = true;
            }
        }
        return outside;
    }


    public void schedule(@NotNull ScheduledExecutorService executor) {
        executor.scheduleAtFixedRate(this, 500L, 500L, TimeUnit.MILLISECONDS);
    }
}