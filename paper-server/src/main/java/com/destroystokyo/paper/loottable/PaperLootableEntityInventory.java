package com.destroystokyo.paper.loottable;

import net.minecraft.world.level.Level;
import org.bukkit.entity.Entity;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

@DefaultQualifier(NonNull.class)
public interface PaperLootableEntityInventory extends LootableEntityInventory, PaperLootableInventory, PaperLootableEntity {

    /* PaperLootableInventory */
    @Override
    default Level getNMSWorld() {
        return this.getHandle().level();
    }

    @Override
    default PaperLootableInventoryData lootableDataForAPI() {
        return this.getHandle().lootableData();
    }

    /* LootableEntityInventory */
    default Entity getEntity() {
        return ((net.minecraft.world.entity.Entity) this.getHandle()).getBukkitEntity();
    }
}
