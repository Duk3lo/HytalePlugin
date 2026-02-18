package com.astral.server.permission;

import com.hypixel.hytale.server.core.permissions.PermissionsModule;

import java.util.Set;

public final class DefPermission {
    public static final String BREAK_BLOCKS = "astral.breakblocks";
    public static final String ADD_BLOCKS = "astral.addblocks";
    public static final String PICKUP_ITEMS = "astral.pickupitems";
    public static final String DROP_ITEMS = "astral.dropitems";
    public static final String DAMAGE = "astral.damage";
    public static final String INVENTORY_CHANGE = "astral.inventorychange";

    private static final PermissionsModule permissions = PermissionsModule.get();

    public static void register(){
        permissions.addGroupPermission("lobby", Set.of(BREAK_BLOCKS, ADD_BLOCKS,  PICKUP_ITEMS, DROP_ITEMS, DAMAGE, INVENTORY_CHANGE));
    }
}
