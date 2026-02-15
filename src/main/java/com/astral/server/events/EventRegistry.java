package com.astral.server.events;

import com.astral.server.events.custom.*;
import com.astral.server.events.event.Join;
import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public final class EventRegistry {
    public static void registerEvents(@NonNullDecl JavaPlugin plugin) {
        Join join = new Join();
        plugin.getEventRegistry().register(PlayerConnectEvent.class, join::onPlayerJoin);
        plugin.getEventRegistry().registerGlobal(PlayerReadyEvent.class, join::onPlayerReady);
        plugin.getEntityStoreRegistry().registerSystem(new CanBreakBlock());
        plugin.getEntityStoreRegistry().registerSystem(new CanAddBlock());
        plugin.getEntityStoreRegistry().registerSystem(new CanDropItemRequest());
        plugin.getEntityStoreRegistry().registerSystem(new CanPickUpItem());
        plugin.getEntityStoreRegistry().registerSystem(new CanDamage());
    }
}
