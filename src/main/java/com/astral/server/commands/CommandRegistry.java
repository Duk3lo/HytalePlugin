package com.astral.server.commands;

import com.astral.server.commands.command.About;
import com.astral.server.commands.command.OpenMenuMode;
import com.astral.server.commands.command.ReloadPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public final class CommandRegistry {

    public static void registerCommands(@NonNullDecl JavaPlugin plugin) {

        plugin.getCommandRegistry().registerCommand(new About("about", "this is simple command", false));
        plugin.getCommandRegistry().registerCommand(new OpenMenuMode("astMen", "Open the menu of games", false));
        plugin.getCommandRegistry().registerCommand(new ReloadPlugin("reload_plugin_astral", "refresh serverUI", false));
    }
}
