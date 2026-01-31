package com.astral.server.events.event;

import com.astral.server.Main;
import com.astral.server.config.PluginConfig;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.util.EventTitleUtil;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;


public final class Join {

    public void onPlayerJoin(@NonNullDecl PlayerConnectEvent event) {
        PlayerRef playerRef = event.getPlayerRef();

        PluginConfig config = Main.getInstance().getPluginConfig();
        PluginConfig.Join join = config.getJoin();

        if (!join.isEnable()) {
            return;
        }

        EventTitleUtil.showEventTitleToPlayer(
                playerRef,
                Message.raw(join.getTopMessage()),
                Message.raw(join.getBottomMessage()),
                true
        );
    }
}