package com.astral.server.ui;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime;
import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType;
import com.hypixel.hytale.protocol.packets.interface_.Page;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.entities.player.pages.InteractiveCustomUIPage;
import com.hypixel.hytale.server.core.ui.builder.EventData;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import javax.annotation.Nonnull;

public final class ServerMenu extends InteractiveCustomUIPage<ServerMenu.MenuEventData> {

    public static class MenuEventData {
        public String action;
        public static final BuilderCodec<MenuEventData> CODEC =
                BuilderCodec.builder(MenuEventData.class, MenuEventData::new)
                        .append(
                                new KeyedCodec<>("Action", Codec.STRING),
                                (data, value) -> data.action = value,
                                data -> data.action
                        )
                        .add()
                        .build();
    }

    public ServerMenu(@NonNullDecl PlayerRef playerRef) {
        super(playerRef, CustomPageLifetime.CanDismissOrCloseThroughInteraction, MenuEventData.CODEC);
    }

    @Override
    public void build(@NonNullDecl Ref<EntityStore> ref, @NonNullDecl UICommandBuilder uiCommandBuilder, @NonNullDecl UIEventBuilder uiEventBuilder, @NonNullDecl Store<EntityStore> store) {
        uiCommandBuilder.append("Astral/Menu.ui");
        uiEventBuilder.addEventBinding(CustomUIEventBindingType.Activating, "#Vanilla", new EventData().append("Action", "Vanilla"), false);
        uiEventBuilder.addEventBinding(CustomUIEventBindingType.Activating, "#BedWars", new EventData().append("Action", "BedWars"), false);
        uiEventBuilder.addEventBinding(CustomUIEventBindingType.Activating, "#SkyWars", new EventData().append("Action", "SkyWars"), false);
        uiEventBuilder.addEventBinding(CustomUIEventBindingType.Activating, "#Parkour", new EventData().append("Action", "Parkour"), false);
        uiEventBuilder.addEventBinding(CustomUIEventBindingType.Activating, "#PvP", new EventData().append("Action", "PvP"), false);
        uiEventBuilder.addEventBinding(CustomUIEventBindingType.Activating, "#Eventos", new EventData().append("Action", "Eventos"), false);
        uiEventBuilder.addEventBinding(CustomUIEventBindingType.Activating, "#CloseButton", new EventData().append("Action", "CloseButton"), false);
    }

    @Override
    public void handleDataEvent(
            @Nonnull Ref<EntityStore> ref,
            @Nonnull Store<EntityStore> store,
            @Nonnull MenuEventData data
    ) {
        super.handleDataEvent(ref, store, data);

        Player player = store.getComponent(ref, Player.getComponentType());
        if (player == null) return;

        switch (data.action) {

            case "Vanilla":
                player.sendMessage(Message.raw("Vanilla está en desarrollo (WIP)"));
                break;

            case "BedWars":
                player.sendMessage(Message.raw("BedWars está en desarrollo (WIP)"));
                break;

            case "SkyWars":
                player.sendMessage(Message.raw("SkyWars está en desarrollo (WIP)"));
                break;

            case "Parkour":
                player.sendMessage(Message.raw("Parkour está en desarrollo (WIP)"));
                break;

            case "PvP":
                player.sendMessage(Message.raw("PvP está en desarrollo (WIP)"));
                break;

            case "Eventos":
                player.sendMessage(Message.raw("Eventos está en desarrollo (WIP)"));
                break;

            case "CloseButton":
                player.getPageManager().setPage(ref, store, Page.None);
                break;

            default:
                player.sendMessage(Message.raw("Modo no reconocido"));
                break;
        }
    }

    @Override
    protected void close() {

    }
}