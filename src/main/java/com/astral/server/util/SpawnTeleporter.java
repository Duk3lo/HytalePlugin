package com.astral.server.util;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.vector.Transform;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3f;
import com.hypixel.hytale.server.core.modules.entity.teleport.Teleport;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import java.util.Objects;

public final class SpawnTeleporter {

    public static void teleportToSpawn(PlayerRef playerRef, World world ,boolean keepRotation) {

        if (playerRef == null) return;

        Transform spawnTransform = Objects
                .requireNonNull(world.getWorldConfig().getSpawnProvider())
                .getSpawnPoint(world, world.getWorldConfig().getUuid());

        Ref<EntityStore> ref = playerRef.getReference();
        if (ref == null || !ref.isValid()) return;

        Store<EntityStore> store = ref.getStore();

        world.execute(() -> {

            Vector3d spawnPos = spawnTransform.getPosition();

            float pitch;
            float yaw;
            float roll;

            if (keepRotation) {
                Transform playerTransform = playerRef.getTransform();

                pitch = playerTransform.getRotation().getX();
                yaw   = playerTransform.getRotation().getY();
                roll  = playerTransform.getRotation().getZ();

            } else {
                pitch = spawnTransform.getRotation().getX();
                yaw   = spawnTransform.getRotation().getY();
                roll  = spawnTransform.getRotation().getZ();
            }

            Teleport teleport = Teleport.createForPlayer(
                    world,
                    spawnPos,
                    new Vector3f(pitch, yaw, roll)
            );

            store.addComponent(ref, Teleport.getComponentType(), teleport);
        });
    }
}