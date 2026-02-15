package com.astral.server.events.custom;

import com.astral.server.permission.DefPermission;
import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.EntityEventSystem;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.ecs.PlaceBlockEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.jetbrains.annotations.NotNull;

public final class CanAddBlock extends EntityEventSystem<EntityStore, PlaceBlockEvent> {
    public CanAddBlock() {
        super(PlaceBlockEvent.class);
    }

    @Override
    public void handle(int i,
                       @NotNull ArchetypeChunk<EntityStore> archetypeChunk,
                       @NotNull Store<EntityStore> store,
                       @NotNull CommandBuffer<EntityStore> commandBuffer,
                       @NotNull PlaceBlockEvent placeBlockEvent) {
        Ref<EntityStore> ref = archetypeChunk.getReferenceTo(i);
        Player player = store.getComponent(ref, Player.getComponentType());
        if (player == null) return;
        if (!player.hasPermission(DefPermission.ADD_BLOCKS)) {
            placeBlockEvent.setCancelled(true);
        }
    }

    @Override
    public @NotNull Query<EntityStore> getQuery() {
        return PlayerRef.getComponentType();
    }
}