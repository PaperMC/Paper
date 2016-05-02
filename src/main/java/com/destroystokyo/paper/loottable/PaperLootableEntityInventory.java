package com.destroystokyo.paper.loottable;

import net.minecraft.server.World;
import org.bukkit.entity.Entity;

public interface PaperLootableEntityInventory extends LootableEntityInventory, PaperLootableInventory {

    net.minecraft.server.Entity getHandle();

    @Override
    default LootableInventory getAPILootableInventory() {
        return this;
    }

    default Entity getEntity() {
        return getHandle().getBukkitEntity();
    }

    @Override
    default World getNMSWorld() {
        return getHandle().getWorld();
    }

    @Override
    default PaperLootableInventoryData getLootableData() {
        return getHandle().lootableData;
    }
}
