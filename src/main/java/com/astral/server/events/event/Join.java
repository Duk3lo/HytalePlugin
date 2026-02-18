package com.astral.server.events.event;

import com.astral.server.Main;
import com.astral.server.config.PluginConfig;
import com.astral.server.util.ItemsToConfig;
import com.astral.server.util.SpawnTeleporter;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.EventTitleUtil;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public final class Join {

    public void onPlayerJoin(@NonNullDecl PlayerConnectEvent event) {
        PlayerRef playerRef = event.getPlayerRef();
        PluginConfig config = Main.getInstance().getPluginConfig();
        PluginConfig.Join join = config.getJoin();
        if (join.isEnable()) {
            EventTitleUtil.showEventTitleToPlayer(
                    playerRef,
                    Message.raw(join.getTopMessage()),
                    Message.raw(join.getBottomMessage()),
                    true
            );
        }
    }

    public void onPlayerReady(@NonNullDecl PlayerReadyEvent event) {
        final World world = event.getPlayer().getWorld();
        final Player player = event.getPlayer();
        Store<EntityStore> store = event.getPlayerRef().getStore();
        Ref<EntityStore> ref = event.getPlayerRef();
        PlayerRef playerRef = store.getComponent(ref, PlayerRef.getComponentType());
        if (playerRef==null) return;
        SpawnTeleporter.teleportToSpawn(playerRef, world, false);
        ItemsToConfig.LoadItemsToStorage();
        ItemsToConfig.inOriginalSlots(player, playerRef.getUuid());
    }
}