package com.astral.server.commands.command;

import com.astral.server.Main;
import com.astral.server.ui.ServerMenu;
import com.astral.server.ui.ServersStatusService;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.AbstractCommand;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class ReloadPlugin extends AbstractCommand {

    public ReloadPlugin(@NonNullDecl String name,
                        @NonNullDecl String description,
                        boolean requiresConfirmation) {
        super(name, description, requiresConfirmation);
    }

    @NonNullDecl
    @Override
    protected CompletableFuture<Void> execute(@NonNullDecl CommandContext ctx) {

        Main plugin = Main.getInstance();

        plugin.reloadPluginConfig();
        List<String> modes = plugin.getPluginConfig()
                .getMenuLobby()
                .getServers()
                .keySet()
                .stream()
                .toList();

        for (ServerMenu menu : ServersStatusService.getMenus().values()) {
            menu.reloadModes(modes);
        }

        ctx.sender().sendMessage(
                Message.raw("§aConfiguración y menús recargados correctamente")
        );

        return CompletableFuture.completedFuture(null);
    }
}
