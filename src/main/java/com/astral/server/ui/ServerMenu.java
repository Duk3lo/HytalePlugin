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
import java.util.*;

public final class ServerMenu extends InteractiveCustomUIPage<ServerMenu.MenuEventData> {

    private final List<String> modes;
    private final Map<String, Integer> modeIndex = new HashMap<>();

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

    public ServerMenu(@NonNullDecl PlayerRef playerRef, Collection<String> modes) {
        super(playerRef, CustomPageLifetime.CanDismissOrCloseThroughInteraction, MenuEventData.CODEC);
        this.modes = new ArrayList<>(modes);
        ServersStatusService.addMenu(playerRef.getUuid(), this);
    }

    @Override
    public void build(@NonNullDecl Ref<EntityStore> ref,
                      @NonNullDecl UICommandBuilder ui,
                      @NonNullDecl UIEventBuilder events,
                      @NonNullDecl Store<EntityStore> store) {

        ui.append("Astral/Menu.ui");
        ui.clear("#Display");

        int i = 0;
        for (String mode : modes) {

            ui.append("#Display", "Astral/ModeEntry.ui");
            String selector = "#Display[" + i + "]";

            ui.set(selector + " #ModeButton.Text", mode);
            ui.set(selector + " #Stat.Text", "Jugadores");
            ui.set(selector + " #Count.Text", "0/0");

            modeIndex.put(mode, i);

            events.addEventBinding(
                    CustomUIEventBindingType.Activating,
                    selector + " #ModeButton",
                    new EventData().append("Action", mode),
                    false
            );
            i++;
        }

        events.addEventBinding(
                CustomUIEventBindingType.Activating,
                "#CloseButton",
                new EventData().append("Action", "CloseButton"),
                false
        );
    }

    @Override
    public void handleDataEvent(@Nonnull Ref<EntityStore> ref,
                                @Nonnull Store<EntityStore> store,
                                @Nonnull MenuEventData data) {
        super.handleDataEvent(ref, store, data);

        Player player = store.getComponent(ref, Player.getComponentType());
        if (player == null) return;

        String act = data.action;
        if (act == null) return;

        if ("CloseButton".equals(act)) {
            player.getPageManager().setPage(ref, store, Page.None);
            return;
        }

        player.sendMessage(Message.raw(act + " está en desarrollo (WIP)"));
    }

    public void updateMode(
            String modeName,
            String statText,
            String countText
    ) {
        Integer idx = modeIndex.get(modeName);
        if (idx == null) return;

        UICommandBuilder builder = new UICommandBuilder();
        String base = "#Display[" + idx + "]";

        if (countText != null) {
            builder.set(base + " #Count.Text", countText);
        }

        if (statText != null) {
            builder.set(base + " #Stat.Text", statText);
        }

        sendUpdate(builder, false);
    }

    @Override
    public void onDismiss(@NonNullDecl Ref<EntityStore> ref, @NonNullDecl Store<EntityStore> store) {
        super.onDismiss(ref, store);
        final PlayerRef player = store.getComponent(ref, PlayerRef.getComponentType());
        if (player == null) return;
        ServersStatusService.removeMenu(player.getUuid());
    }
}
