package com.astral.server;

import com.astral.server.commands.CommandRegistry;
import com.astral.server.config.LoadConfig;
import com.astral.server.config.PluginConfig;
import com.astral.server.events.EventRegistry;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.util.Config;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public final class Main extends JavaPlugin {


    private static Main INSTANCE;

    private final Config<PluginConfig> configFile;
    private PluginConfig config;

    public Main(@NonNullDecl JavaPluginInit init) {
        super(init);
        configFile = this.withConfig("plugin-config", PluginConfig.CODEC);
    }

    @Override
    protected void setup() {
        INSTANCE = this;
        this.config = configFile.load().join();
        LoadConfig.build(this, configFile);
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

    public static Main getInstance() {
        return INSTANCE;
    }

    public PluginConfig getPluginConfig() {
        return config;
    }

    public void reloadPluginConfig() {
        this.config = configFile.load().join();
        getLogger().atInfo().log("plugin-config.json recargado");
    }

}