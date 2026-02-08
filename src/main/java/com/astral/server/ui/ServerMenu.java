package com.astral.server.ui;

import com.astral.server.Main;
import com.astral.server.config.PluginConfig;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.logger.HytaleLogger;
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

import java.util.*;

public final class ServerMenu extends InteractiveCustomUIPage<ServerMenu.MenuEventData> {

    private final List<String> modes;
    private final Map<String, Integer> modeIndex = new HashMap<>();

    public static class MenuEventData {
        public String action;

        public static final BuilderCodec<MenuEventData> CODEC =
                BuilderCodec.builder(MenuEventData.class, MenuEventData::new)
                        .append(new KeyedCodec<>("Action", Codec.STRING),
                                (d, v) -> d.action = v,
                                d -> d.action)
                        .add()
                        .build();
    }

    public ServerMenu(PlayerRef playerRef, Collection<String> modes) {
        super(playerRef, CustomPageLifetime.CanDismissOrCloseThroughInteraction, MenuEventData.CODEC);
        this.modes = new ArrayList<>(modes);
        ServersStatusService.addMenu(playerRef.getUuid(), this);
    }

    @Override
    public void build(@NonNullDecl Ref<EntityStore> ref,
                      @NonNullDecl UICommandBuilder ui,
                      @NonNullDecl UIEventBuilder events,
                      @NonNullDecl Store<EntityStore> store) {

        PluginConfig config = Main.getInstance().getPluginConfig();
        PluginConfig.StatusInfo offline = config.getMenuLobby().getStatus().getOffline();

        ui.append("Astral/Menu.ui");
        ui.clear("#Display");

        int i = 0;
        for (String mode : modes) {

            ui.append("#Display", "Astral/ModeEntry.ui");
            String base = "#Display[" + i + "]";

            ui.set(base + " #ModeButton.Text", mode);
            ui.set(base + " #Stat.Text", offline.getText());
            ui.set(base + " #Stat.Style.TextColor", offline.getColor());
            ui.set(base + " #Count.Text", offline.getFormatCount());
            ui.set(base + " #Count.Style.TextColor", offline.getCountColor());

            modeIndex.put(mode, i);

            events.addEventBinding(
                    CustomUIEventBindingType.Activating,
                    base + " #ModeButton",
                    new EventData().append("Action", mode),
                    false
            );
            i++;
        }
        connectRedisData(ui);
        events.addEventBinding(
                CustomUIEventBindingType.Activating,
                "#CloseButton",
                new EventData().append("Action", "CloseButton"),
                false
        );
    }

    public void updateMode(String mode, boolean online, int actual, int max) {
        Integer idx = modeIndex.get(mode);
        if (idx == null) return;

        PluginConfig config = Main.getInstance().getPluginConfig();
        PluginConfig.StatusInfo info = online
                ? config.getMenuLobby().getStatus().getOnline()
                : config.getMenuLobby().getStatus().getOffline();

        UICommandBuilder ui = new UICommandBuilder();
        String base = "#Display[" + idx + "]";

        ui.set(base + " #Stat.Text", info.getText());
        ui.set(base + " #Stat.Style.TextColor", info.getColor());

        if (info.getFormatCount() != null) {
            String count = info.getFormatCount()
                    .replace("$actual", String.valueOf(actual))
                    .replace("$max", String.valueOf(max));
            ui.set(base + " #Count.Text", count);
            ui.set(base + " #Count.Style.TextColor", info.getCountColor());
        }

        sendUpdate(ui, false);
    }

    private void connectRedisData(UICommandBuilder ui) {
        PluginConfig config = Main.getInstance().getPluginConfig();
        if (!config.getMenuLobby().getRedis().isEnabled()) return;

        try {
            PluginConfig.StatusInfo onlineInfo = config.getMenuLobby().getStatus().getOnline();
            PluginConfig.StatusInfo offlineInfo = config.getMenuLobby().getStatus().getOffline();

            Map<String, String> counts = RedisMenuCache.getInstance().getCounts();

            for (String mode : modes) {
                Integer idx = modeIndex.get(mode);
                if (idx == null) continue;
                String base = "#Display[" + idx + "]";

                boolean online = counts.containsKey(mode);
                PluginConfig.StatusInfo info = online ? onlineInfo : offlineInfo;

                ui.set(base + " #Stat.Text", info.getText());
                ui.set(base + " #Stat.Style.TextColor", info.getColor());

                if (info.getFormatCount() != null) {
                    if (online) {
                        String raw = counts.get(mode);
                        int players = 0;
                        if (raw != null) {
                            try {
                                players = Integer.parseInt(raw);
                            } catch (NumberFormatException ignored) {
                            }
                        }
                        String count = info.getFormatCount()
                                .replace("$actual", String.valueOf(players))
                                .replace("$max", String.valueOf(100)); // modifica si tienes max real
                        ui.set(base + " #Count.Text", count);
                        ui.set(base + " #Count.Style.TextColor", info.getCountColor());
                    } else {
                        ui.set(base + " #Count.Text", offlineInfo.getFormatCount());
                        ui.set(base + " #Count.Style.TextColor", offlineInfo.getCountColor());
                    }
                }
            }
        } catch (Exception e) {
            HytaleLogger.getLogger().atWarning().log("connectRedisData error: " + e.getMessage());
        }
    }

    @Override
    public void handleDataEvent(@NonNullDecl Ref<EntityStore> ref,
                                @NonNullDecl Store<EntityStore> store,
                                @NonNullDecl MenuEventData data) {

        Player player = store.getComponent(ref, Player.getComponentType());
        if (player == null || data.action == null) return;

        if ("CloseButton".equals(data.action)) {
            player.getPageManager().setPage(ref, store, Page.None);
            return;
        }

        player.sendMessage(Message.raw(data.action + " está en desarrollo"));
    }

    @Override
    public void onDismiss(@NonNullDecl Ref<EntityStore> ref, @NonNullDecl Store<EntityStore> store) {
        PlayerRef player = store.getComponent(ref, PlayerRef.getComponentType());
        if (player != null) {
            ServersStatusService.removeMenu(player.getUuid());
        }
    }

    public void reloadModes(Collection<String> newModes) {

        this.modes.clear();
        this.modes.addAll(newModes);

        this.modeIndex.clear();

        PluginConfig config = Main.getInstance().getPluginConfig();
        PluginConfig.StatusInfo offline = config.getMenuLobby().getStatus().getOffline();

        UICommandBuilder ui = new UICommandBuilder();
        ui.clear("#Display");

        int i = 0;
        for (String mode : modes) {

            ui.append("#Display", "Astral/ModeEntry.ui");
            String base = "#Display[" + i + "]";

            ui.set(base + " #ModeButton.Text", mode);
            ui.set(base + " #Stat.Text", offline.getText());
            ui.set(base + " #Count.Text", "");

            modeIndex.put(mode, i);
            i++;
        }

        sendUpdate(ui, true);
    }

    public List<String> getModes() {
        return this.modes;
    }

}