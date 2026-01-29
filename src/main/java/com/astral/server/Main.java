package com.astral.server;

import com.astral.server.commands.CommandRegistry;
import com.astral.server.events.EventRegistry;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public final class Main extends JavaPlugin {


    private static JavaPlugin plugin;

    public Main(@NonNullDecl JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup() {
        plugin = this;
        CommandRegistry.registerCommands(plugin);
        EventRegistry.registerEvents(plugin);
        getLogger().atInfo().log("Init Custom Load!");
    }

    @Override
    protected void start() {
        getLogger().atInfo().log("Loaded");
    }

    @Override
    protected void shutdown() {
        getLogger().atInfo().log("Bye Bye");
    }

    public static JavaPlugin getPlugin() {return plugin;}
}