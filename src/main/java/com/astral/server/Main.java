package com.astral.server;

import com.astral.server.commands.CommandRegistry;
import com.astral.server.config.Configuration;
import com.astral.server.events.EventRegistry;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public final class Main extends JavaPlugin {


    public Main(@NonNullDecl JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup() {
        Configuration.build(this);
        CommandRegistry.registerCommands(this);
        EventRegistry.registerEvents(this);
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

}