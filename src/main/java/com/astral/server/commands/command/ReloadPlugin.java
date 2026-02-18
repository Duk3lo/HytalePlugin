package com.astral.server.commands.command;

import com.astral.server.Main;
import com.astral.server.config.PluginConfig;
import com.astral.server.redis.RedisService;
import com.astral.server.ui.ServerMenu;
import com.astral.server.ui.ServersStatusService;
import com.astral.server.util.ItemsToConfig;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.AbstractCommand;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.permissions.HytalePermissions;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.util.Collection;
import java.util.Map;
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
        Map<String, PluginConfig.ServerInfo> modes = plugin.getPluginConfig().getMenuLobby().getServers();

        for (ServerMenu menu : ServersStatusService.getMenus().values()) {
            menu.reloadModes(modes);
        }

        Collection<World> worlds = Universe.get().getWorlds().values();
        for (World world : worlds) {
            world.execute(() -> {
                Collection<PlayerRef> playerRefs = world.getPlayerRefs();
                for (PlayerRef playerRef : playerRefs) {
                    Ref<EntityStore> ref = playerRef.getReference();
                    if (ref == null) continue;
                    Store<EntityStore> store = ref.getStore();
                    Player player = store.getComponent(ref, Player.getComponentType());
                    if (player == null) continue;
                    ItemsToConfig.LoadItemsToStorage();
                    ItemsToConfig.inOriginalSlots(player, playerRef.getUuid());
                }
            });
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
