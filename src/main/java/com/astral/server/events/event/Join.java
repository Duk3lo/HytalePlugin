package com.astral.server.events.event;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.util.EventTitleUtil;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public final class Join {

    public void onPlayerJoin(@NonNullDecl PlayerConnectEvent event) {
        PlayerRef playerRef = event.getPlayerRef();
        EventTitleUtil.showEventTitleToPlayer(
                playerRef,
                Message.raw("Astral"),
                Message.raw("Bienvenido al Servidor. Testing Duk3lo"),
                true);
    }
}