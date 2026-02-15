package com.astral.server;

import com.astral.server.commands.CommandRegistry;
import com.astral.server.config.LoadConfig;
import com.astral.server.config.PluginConfig;
import com.astral.server.events.EventRegistry;
import com.astral.server.events.EventScheduler;
import com.astral.server.permission.DefPermission;
import com.astral.server.redis.RedisService;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.util.Config;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public final class Main extends JavaPlugin {


    private static Main instance;

    private final Config<PluginConfig> configFile;
    private PluginConfig config;
    private RedisService redisService;
    private EventScheduler eventScheduler;

    public Main(@NonNullDecl JavaPluginInit init) {
        super(init);
        configFile = this.withConfig("plugin-config", PluginConfig.CODEC);
    }

    @Override
    protected void setup() {
        instance = this;
        this.config = configFile.load().join();
        PluginConfig.Redis redis = config.getMenuLobby().getRedis();
        if (redis.isEnabled()) {redisService = new RedisService(redis.getRedisHost(), redis.getRedisPort(), redis.getTimeOut(), redis.getRedisPassword(), getLogger());}
        LoadConfig.build(instance, configFile);
        CommandRegistry.registerCommands(instance);
        EventRegistry.registerEvents(instance);
        DefPermission.register();
        getLogger().atInfo().log("Init Custom Load!");
    }

    @Override
    protected void start() {
        eventScheduler = new EventScheduler(instance);
        eventScheduler.startStatusPosition(15, 150);
        getLogger().atInfo().log("Loaded");
    }

    @Override
    protected void shutdown() {
        if (this.eventScheduler != null) {
            this.eventScheduler.stop();
        }
        getLogger().atInfo().log("Bye Bye");
    }

    public static Main getInstance() {
        return instance;
    }

    public PluginConfig getPluginConfig() {
        return config;
    }

    public void reloadPluginConfig() {
        this.config = configFile.load().join();
        getLogger().atInfo().log("plugin-config.json recargado");
    }

    public RedisService getRedisService() {
        return redisService;
    }

    public void setRedisService(RedisService redisService) {
        this.redisService = redisService;
    }
}