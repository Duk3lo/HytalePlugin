package com.astral.server.events.custom;


import com.astral.server.permission.DefPermission;
import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.DelayedEntitySystem;
import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entity.component.Invulnerable;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.jetbrains.annotations.NotNull;

public final class CanDamage extends DelayedEntitySystem<EntityStore> {
    public CanDamage() {
        super(1.5f);
    }

    @Override
    public void tick(float v, int i, @NotNull ArchetypeChunk<EntityStore> archetypeChunk, @NotNull Store<EntityStore> store, @NotNull CommandBuffer<EntityStore> commandBuffer) {
        Ref<EntityStore> ref = archetypeChunk.getReferenceTo(i);
        Player player = store.getComponent(ref, Player.getComponentType());
        if (player == null) return;
        World world = player.getWorld();
        if (world == null) return;
        world.execute(()->{
            if (player.getGameMode().equals(GameMode.Adventure)) {
                if (!player.hasPermission(DefPermission.DAMAGE)) store.putComponent(ref, Invulnerable.getComponentType(), Invulnerable.INSTANCE);
                else store.removeComponentIfExists(ref, Invulnerable.getComponentType());
            }
        });
    }

    @Override
    public @NotNull Query<EntityStore> getQuery() {
        return PlayerRef.getComponentType();
    }
}
