package com.astral.server.events.custom;

import com.astral.server.permission.DefPermission;
import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.EntityEventSystem;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.ecs.InteractivelyPickupItemEvent;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.jetbrains.annotations.NotNull;

public final class CanPickUpItem extends EntityEventSystem<EntityStore, InteractivelyPickupItemEvent> {

    public CanPickUpItem() {
        super(InteractivelyPickupItemEvent.class);
    }

    @Override
    public void handle(int i,
                       @NotNull ArchetypeChunk<EntityStore> archetypeChunk,
                       @NotNull Store<EntityStore> store,
                       @NotNull CommandBuffer<EntityStore> commandBuffer,
                       @NotNull InteractivelyPickupItemEvent interactivelyPickupItemEvent) {
        Ref<EntityStore> ref = archetypeChunk.getReferenceTo(i);
        Player player = store.getComponent(ref, Player.getComponentType());
        if (player != null) {
            if (!player.hasPermission(DefPermission.PICKUP_ITEMS)) {
                interactivelyPickupItemEvent.setItemStack(ItemStack.EMPTY);
                interactivelyPickupItemEvent.setCancelled(true);
            }
        }

    }
    public @NotNull Query<EntityStore> getQuery() {
        return PlayerRef.getComponentType();
    }

}
