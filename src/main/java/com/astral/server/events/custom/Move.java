package com.astral.server.events.custom;

import com.astral.server.Main;
import com.astral.server.config.PluginConfig;
import com.astral.server.util.SpawnTeleporter;
import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.DelayedEntitySystem;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public final class Move  extends DelayedEntitySystem<EntityStore> {

    private double minY;
    private double maxY;
    private double minX;
    private double maxX;
    private double minZ;
    private double maxZ;

    public Move() {
        super(0.5F);
    }

    @Override
    public void tick(float v, int i, @NotNull ArchetypeChunk<EntityStore> archetypeChunk, @NotNull Store<EntityStore> store, @NotNull CommandBuffer<EntityStore> commandBuffer) {
        Ref<EntityStore> ref = archetypeChunk.getReferenceTo(i);
        PlayerRef playerRef = store.getComponent(ref, PlayerRef.getComponentType());
        PluginConfig config = Main.getInstance().getPluginConfig();
        PluginConfig.Limits limits = config.getLimits();
        boolean enabled = limits.isEnable();
        if (!enabled) {return;}
        if (playerRef == null) return;
        UUID worldUUID = playerRef.getWorldUuid();
        if (worldUUID == null) return;
        World world = Universe.get().getWorld(worldUUID);
        if (world == null) return;
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

        Vector3d position = playerRef.getTransform().getPosition();
        boolean outside = isABoolean(position);
        if (outside) {
            SpawnTeleporter.teleportToSpawn(playerRef, world, true);
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

    @Override
    public @NotNull Query<EntityStore> getQuery() {
        return PlayerRef.getComponentType();
    }
}
