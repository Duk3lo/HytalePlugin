package com.astral.server.events.custom;

import com.astral.server.permission.DefPermission;
import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.EntityEventSystem;
import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.ecs.ChangeGameModeEvent;
import com.hypixel.hytale.server.core.modules.entity.component.Invulnerable;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.jetbrains.annotations.NotNull;

public final class GameModeChange extends EntityEventSystem<EntityStore, ChangeGameModeEvent> {
    public GameModeChange() {
        super(ChangeGameModeEvent.class);
    }

    @Override
    public void handle(int i, @NotNull ArchetypeChunk<EntityStore> archetypeChunk, @NotNull Store<EntityStore> store, @NotNull CommandBuffer<EntityStore> commandBuffer, @NotNull ChangeGameModeEvent event) {
        Ref<EntityStore> ref = archetypeChunk.getReferenceTo(i);
        Player player = store.getComponent(ref, Player.getComponentType());
        if (player != null && event.getGameMode() == GameMode.Adventure) {
            World world = player.getWorld();
            if (world!=null)
                world.execute(()->{
                    if (!player.hasPermission(DefPermission.DAMAGE)) store.putComponent(ref, Invulnerable.getComponentType(), Invulnerable.INSTANCE);
                });
        }
    }

    @Override
    public @NotNull Query<EntityStore> getQuery() {
        return PlayerRef.getComponentType();
    }
}
