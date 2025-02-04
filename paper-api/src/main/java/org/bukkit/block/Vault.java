package org.bukkit.block;

import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootTable;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import java.util.Set;
import java.util.UUID;

/**
 * Represents a captured state of a vault.
 */
@NullMarked
public interface Vault extends TileState {
    /**
     * Gets the range in blocks at which this vault will become active when a player is near.
     *
     * @return This vault's activation range.
     */
    double getActivationRange();

    /**
     * Sets the range in blocks at which the vault will become active when a player is near.
     *
     * @param activationRange The new activation range.
     * @throws IllegalArgumentException if the new range is not a number, or if the new range is more than {@link #getDeactivationRange()}.
     */
    void setActivationRange(double activationRange);

    /**
     * Gets the range in blocks at which this vault will become inactive when a player is not near.
     *
     * @return This vault's deactivation range.
     */
    double getDeactivationRange();

    /**
     * Sets the range in blocks at which this vault will become inactive when a player is not near.
     *
     * @param deactivationRange The new deactivation range
     * @throws IllegalArgumentException if the new range is not a number, or if the new range is less than {@link #getActivationRange()}.
     */
    void setDeactivationRange(double deactivationRange);

    /**
     * Gets the {@link ItemStack} that players must use to unlock this vault.
     *
     * @return The item that players must use to unlock this vault.
     */
    ItemStack getKeyItem();

    /**
     * Sets the {@link ItemStack} that players must use to unlock this vault.
     *
     * @param key The key item.
     */
    void setKeyItem(ItemStack key);

    /**
     * Gets the {@link LootTable} that this vault will select rewards from.
     *
     * @return The loot table.
     */
    LootTable getLootTable();

    /**
     * Sets the {@link LootTable} that this vault will select rewards from.
     *
     * @param lootTable The new loot table.
     */
    void setLootTable(LootTable lootTable);

    /**
     * Gets the loot table that this vault will display items from.
     * <p>
     * Falls back to the regular {@link #getLootTable() loot table} if unset.
     *
     * @return The {@link LootTable} that will be used to display items.
     */
    @Nullable
    LootTable getDisplayLootTable();

    /**
     * Sets the loot table that this vault will display items from.
     *
     * @param lootTable The new display loot table, or {@code null} to clear this display override.
     */
    void setDisplayLootTable(@Nullable LootTable lootTable);

    /**
     * Gets the next time (in {@link World#getGameTime() game time}) that this vault block will be updated/ticked at.
     *
     * @return The next time that this vault block will be updated/ticked at.
     * @see World#getGameTime()
     */
    long getNextStateUpdateTime();

    /**
     * Sets the next time that this vault block will be updated/ticked at, if this value is less than or equals to the current
     * {@link World#getGameTime()}, then it will be updated in the first possible tick.
     *
     * @param nextStateUpdateTime The next time that this vault block will be updated/ticked at.
     * @see World#getGameTime()
     */
    void setNextStateUpdateTime(long nextStateUpdateTime);

    /**
     * Gets the players who have used a key on this vault and unlocked it.
     *
     * @return An unmodifiable set of player uuids.
     *
     * @apiNote Only the most recent 128 player UUIDs will be stored by vault blocks.
     */
    @Unmodifiable
    Set<UUID> getRewardedPlayers();

    /**
     * Adds a player as rewarded for this vault.
     *
     * @param playerUUID The player's uuid.
     * @return {@code true} if this player was previously not rewarded, and has been added as a result of this operation.
     *
     * @apiNote Only the most recent 128 player UUIDs will be stored by vault blocks. Attempting to add more will result in
     *      the first player UUID being removed.
     */
    boolean addRewardedPlayer(UUID playerUUID);

    /**
     * Removes a player as rewarded for this vault, allowing them to use a {@link #getKeyItem() key item} again to receive rewards.
     *
     * @param playerUUID The player's uuid.
     * @return {@code true} if this player was previously rewarded, and has been removed as a result of this operation.
     *
     * @apiNote Only the most recent 128 player UUIDs will be stored by vault blocks.
     */
    boolean removeRewardedPlayer(UUID playerUUID);
}
