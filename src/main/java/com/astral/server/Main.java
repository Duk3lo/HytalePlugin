package com.astral.server;

import com.astral.server.commands.CommandRegistry;
import com.astral.server.config.ItemsConfig;
import com.astral.server.config.LoadConfig;

import com.astral.server.config.PluginConfig;
import com.astral.server.events.EventRegistry;
import com.astral.server.permission.DefPermission;
import com.astral.server.redis.RedisService;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.util.Config;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.util.HashMap;
import java.util.Map;

public final class Main extends JavaPlugin {


    private static Main instance;

    private final Config<PluginConfig> configFile;
    private final Config<ItemsConfig> configItemsFile;
    private PluginConfig config;
    private ItemsConfig itemsConfig;
    private RedisService redisService;

    private static final String pluginConfig = "plugin-config";
    private static final String itemConfig = "item-config";

    public Main(@NonNullDecl JavaPluginInit init) {
        super(init);
        configFile =  withConfig(pluginConfig, PluginConfig.CODEC);
        configItemsFile = withConfig(itemConfig, ItemsConfig.CODEC);
    }

    @Override
    protected void setup() {
        instance = this;
        config = configFile.load().join();
        itemsConfig = configItemsFile.load().join();
        Map<String, Config<?>> configs = new HashMap<>();
        configs.put(pluginConfig, configFile);
        configs.put(itemConfig, configItemsFile);
        LoadConfig.build(instance, configs);
        PluginConfig.Redis redis = config.getMenuLobby().getRedis();
        if (redis.isEnabled()) {redisService = new RedisService(redis.getRedisHost(), redis.getRedisPort(), redis.getTimeOut(), redis.getRedisPassword(), getLogger());}
        CommandRegistry.registerCommands(instance);
        EventRegistry.registerEvents(instance);
        DefPermission.register();
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
        return instance;
    }

    public PluginConfig getPluginConfig() {
        return config;
    }

    public ItemsConfig getItemsConfig() {
        return itemsConfig;
    }

    public void reloadPluginConfig() {
        config = configFile.load().join();
        itemsConfig = configItemsFile.load().join();
        getLogger().atInfo().log("configuraciones recargadas");
    }

    public RedisService getRedisService() {
        return redisService;
    }

    public void setRedisService(RedisService redisService) {
        this.redisService = redisService;
    }
}