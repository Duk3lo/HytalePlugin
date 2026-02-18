package com.astral.server.events.event;

import com.astral.server.permission.DefPermission;
import com.astral.server.util.ItemsToConfig;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.entity.LivingEntity;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.entity.LivingEntityInventoryChangeEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.jetbrains.annotations.NotNull;

public final class InventoryEvent {
    public void OnPlayerInventoryChange(@NotNull LivingEntityInventoryChangeEvent event) {
        LivingEntity entity = event.getEntity();
        Ref<EntityStore> ref = entity.getReference();
        if (ref==null) return;
        Store<EntityStore> store = ref.getStore();
        PlayerRef playerRef = store.getComponent(ref, PlayerRef.getComponentType());
        if (playerRef==null) return;
        Player player = store.getComponent(ref, Player.getComponentType());
        if (player==null) return;
        if(!player.hasPermission(DefPermission.INVENTORY_CHANGE)) ItemsToConfig.inOriginalSlots(player, playerRef.getUuid());
    }
}
