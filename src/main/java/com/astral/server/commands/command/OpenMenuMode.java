package com.astral.server.commands.command;

import com.astral.server.ui.ServerMenu;
import com.astral.server.ui.ServersStatusService;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.util.Objects;

public final class OpenMenuMode extends AbstractPlayerCommand {


    public OpenMenuMode(@NonNullDecl String name, @NonNullDecl String description, boolean requiresConfirmation) {
        super(name, description, requiresConfirmation);
    }

    @Override
    protected void execute(@NonNullDecl CommandContext commandContext,
                           @NonNullDecl Store<EntityStore> store,
                           @NonNullDecl Ref<EntityStore> ref,
                           @NonNullDecl PlayerRef playerRef,
                           @NonNullDecl World world) {
        Player player = store.getComponent(ref, Player.getComponentType());
        ServerMenu page = new ServerMenu(playerRef);
        ServersStatusService.addMenu(playerRef.getUuid(), page);
        Objects.requireNonNull(player).getPageManager().openCustomPage(ref, store, page);
    }
}
