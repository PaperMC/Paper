package com.destroystokyo.paper.loottable;

import net.minecraft.server.World;
import org.bukkit.loot.Lootable;

import java.util.UUID;

public interface PaperLootableInventory extends LootableInventory, Lootable {

    PaperLootableInventoryData getLootableData();
    LootableInventory getAPILootableInventory();

    World getNMSWorld();

    default org.bukkit.World getBukkitWorld() {
        return getNMSWorld().getWorld();
    }

    @Override
    default boolean isRefillEnabled() {
        return getNMSWorld().paperConfig.autoReplenishLootables;
    }

    @Override
    default boolean hasBeenFilled() {
        return getLastFilled() != -1;
    }

    @Override
    default boolean hasPlayerLooted(UUID player) {
        return getLootableData().hasPlayerLooted(player);
    }

    @Override
    default Long getLastLooted(UUID player) {
        return getLootableData().getLastLooted(player);
    }

    @Override
    default boolean setHasPlayerLooted(UUID player, boolean looted) {
        final boolean hasLooted = hasPlayerLooted(player);
        if (hasLooted != looted) {
            getLootableData().setPlayerLootedState(player, looted);
        }
        return hasLooted;
    }

    @Override
    default boolean hasPendingRefill() {
        long nextRefill = getLootableData().getNextRefill();
        return nextRefill != -1 && nextRefill > getLootableData().getLastFill();
    }

    @Override
    default long getLastFilled() {
        return getLootableData().getLastFill();
    }

    @Override
    default long getNextRefill() {
        return getLootableData().getNextRefill();
    }

    @Override
    default long setNextRefill(long refillAt) {
        if (refillAt < -1) {
            refillAt = -1;
        }
        return getLootableData().setNextRefill(refillAt);
    }
}
