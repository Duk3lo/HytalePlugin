package com.astral.server.commands.command;

import com.astral.server.ui.ServerMenu;
import com.astral.server.ui.ServersStatusService;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.AbstractCommand;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.CommandSender;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class ReloadModesCommand extends AbstractCommand {

    public ReloadModesCommand(@NonNullDecl String name, @NonNullDecl String description, boolean requiresConfirmation) {
        super(name, description, requiresConfirmation);
    }

    @NonNullDecl
    @Override
    protected  CompletableFuture<Void> execute(@NonNullDecl CommandContext commandContext) {
        List<String> modes = List.of(
                "Vanilla",
                "SkyWars",
                "Parkour",
                "PvP"
        );
        for (ServerMenu menu : ServersStatusService.getMenus().values()) {
            menu.reloadModes(modes);
        }
        CommandSender sender = commandContext.sender();
        sender.sendMessage(Message.raw("§aMenús actualizados correctamente"));

        return CompletableFuture.completedFuture(null);
    }
}
