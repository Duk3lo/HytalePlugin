package com.astral.server.commands.command;

import com.astral.server.Main;
import com.astral.server.config.PluginConfig;
import com.astral.server.redis.RedisService;
import com.astral.server.ui.ServerMenu;
import com.astral.server.ui.ServersStatusService;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.AbstractCommand;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.permissions.HytalePermissions;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class ReloadPlugin extends AbstractCommand {

    public ReloadPlugin(@NonNullDecl String name,
                        @NonNullDecl String description,
                        boolean requiresConfirmation) {
        super(name, description, requiresConfirmation);
        requirePermission(HytalePermissions.fromCommand("astral.reloadplugin"));
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

        PluginConfig.Redis redis = plugin.getPluginConfig().getMenuLobby().getRedis();
        if (redis.isEnabled()) {
            RedisService redisService = new RedisService(redis.getRedisHost(), redis.getRedisPort(), redis.getTimeOut(), redis.getRedisPassword(), HytaleLogger.getLogger());
            plugin.setRedisService(redisService);
        }

        ctx.sender().sendMessage(
                Message.raw("Configuración y menús recargados correctamente")
        );

        return CompletableFuture.completedFuture(null);
    }
}
