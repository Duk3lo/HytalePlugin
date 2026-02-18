package com.astral.server.events.interactions.items;

import com.astral.server.Main;
import com.astral.server.config.PluginConfig;
import com.astral.server.ui.ServerMenu;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.InteractionType;
import com.hypixel.hytale.server.core.entity.InteractionContext;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.interaction.interaction.CooldownHandler;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.SimpleInstantInteraction;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public final class MenuItem extends SimpleInstantInteraction {

    public static final BuilderCodec<MenuItem> CODEC =
            BuilderCodec.builder(MenuItem.class, MenuItem::new, SimpleInstantInteraction.CODEC)
                    .build();

    @Override
    protected void firstRun(@NotNull InteractionType interactionType, @NotNull InteractionContext interactionContext, @NotNull CooldownHandler cooldownHandler) {
        Store<EntityStore> store = interactionContext.getOwningEntity().getStore();
        Ref<EntityStore> ref = interactionContext.getOwningEntity();
        PlayerRef playerRef = store.getComponent(ref, PlayerRef.getComponentType());
        Player player = store.getComponent(ref, Player.getComponentType());
        if (player == null) return;
        PluginConfig config = Main.getInstance().getPluginConfig();
        Map<String, PluginConfig.ServerInfo> modes = config.getMenuLobby().getServers();
        ServerMenu menu = new ServerMenu(playerRef, modes);
        player.getPageManager().openCustomPage(ref, store, menu);
    }
}
