package com.destroystokyo.paper.loottable;

import java.util.UUID;
import net.minecraft.world.level.Level;
import org.bukkit.World;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

@DefaultQualifier(NonNull.class)
public interface PaperLootableInventory extends PaperLootable, LootableInventory {

    /* impl */
    PaperLootableInventoryData lootableDataForAPI();

    Level getNMSWorld();

    default World getBukkitWorld() {
        return this.getNMSWorld().getWorld();
    }

    /* LootableInventory */
    @Override
    default boolean isRefillEnabled() {
        return this.getNMSWorld().paperConfig().lootables.autoReplenish;
    }

    @Override
    default boolean hasBeenFilled() {
        return this.getLastFilled() != -1;
    }

    @Override
    default boolean hasPlayerLooted(final UUID player) {
        return this.lootableDataForAPI().hasPlayerLooted(player);
    }

    @Override
    default boolean canPlayerLoot(final UUID player) {
        return this.lootableDataForAPI().canPlayerLoot(player, this.getNMSWorld().paperConfig());
    }

    @Override
    default Long getLastLooted(final UUID player) {
        return this.lootableDataForAPI().getLastLooted(player);
    }

    @Override
    default boolean setHasPlayerLooted(final UUID player, final boolean looted) {
        final boolean hasLooted = this.hasPlayerLooted(player);
        if (hasLooted != looted) {
            this.lootableDataForAPI().setPlayerLootedState(player, looted);
        }
        return hasLooted;
    }

    @Override
    default boolean hasPendingRefill() {
        final long nextRefill = this.lootableDataForAPI().getNextRefill();
        return nextRefill != -1 && nextRefill > this.lootableDataForAPI().getLastFill();
    }

    @Override
    default long getLastFilled() {
        return this.lootableDataForAPI().getLastFill();
    }

    @Override
    default long getNextRefill() {
        return this.lootableDataForAPI().getNextRefill();
    }

    @Override
    default long setNextRefill(long refillAt) {
        if (refillAt < -1) {
            refillAt = -1;
        }
        return this.lootableDataForAPI().setNextRefill(refillAt);
    }
}
