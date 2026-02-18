package com.astral.server.util;

import com.astral.server.Main;
import com.astral.server.config.ItemsConfig;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.Inventory;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.inventory.container.CombinedItemContainer;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class ItemsToConfig {
    private static final Main plugin = Main.getInstance();

    private static final Map<Short, ItemStack> items = new HashMap<>();
    private static final Set<UUID> uuids = new HashSet<>();
    
    public static void LoadItemsToStorage() {
        items.clear();
        for (Map.Entry<String, ItemsConfig.ItemCommand> entry : plugin.getItemsConfig().getItemsMap().entrySet()) {
            ItemsConfig.ItemCommand item = entry.getValue();
            String idItem = item.getId();
            short slot = item.getSlot();
            ItemStack itemStack = new ItemStack(idItem);
            items.put(slot,itemStack);
        }
    }

    public static void inOriginalSlots(@NotNull Player p, UUID uuid) {
        if (uuids.contains(uuid)) {
            return;
        }
        uuids.add(uuid);
        Inventory inventory = p.getInventory();
        inventory.clear();
        CombinedItemContainer combinedItemContainer = inventory.getCombinedBackpackStorageHotbar();
        for(Map.Entry<Short, ItemStack> entry : items.entrySet()) {
            short slot = entry.getKey();
            ItemStack itemStack = entry.getValue();
            combinedItemContainer.setItemStackForSlot(slot,itemStack);
        }
        uuids.remove(uuid);
    }
}
