package org.bukkit.block;

import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootTable;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import java.util.Collection;
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
    LootTable getDisplayedLootTable();

    /**
     * Sets the loot table that this vault will display items from.
     *
     * @param lootTable The new loot table to display, or {@code null} to clear this display override.
     */
    void setDisplayedLootTable(@Nullable LootTable lootTable);

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
     * @return An unmodifiable collection of player uuids.
     *
     * @apiNote Only the most recent 128 player UUIDs will be stored by vault blocks.
     */
    @Unmodifiable
    Collection<UUID> getRewardedPlayers();

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

    /**
     * Returns whether a given player has already been rewarded by this vault.
     *
     * @param playerUUID The player's uuid.
     * @return Whether this player was previously rewarded by this vault.
     */
    boolean hasRewardedPlayer(UUID playerUUID);

    /**
     * Gets an unmodifiable set of "connected players"; players who are inside this vault's activation range and who have not received rewards yet.
     *
     * @apiNote Vaults will only periodically scan for nearby players, so it may take until the next {@link #getNextStateUpdateTime() update time} for this
     * collection to be updated upon a player entering its range.
     *
     * @return An unmodifiable set of connected player uuids.
     */
    @Unmodifiable
    Set<UUID> getConnectedPlayers();

    /**
     * Returns whether a given player is currently connected to this vault.
     *
     * @param playerUUID the player's uuid
     * @return {@code true} if this player is currently connected to this vault.
     *
     * @see #getConnectedPlayers()
     */
    boolean hasConnectedPlayer(UUID playerUUID);

    /**
     * Gets the item currently being displayed inside this vault. Displayed items will automatically cycle between random items from the {@link #getDisplayedLootTable()}
     * or {@link #getLootTable()} loot tables while this vault is active.
     *
     * @return The item currently being displayed inside this vault.
     */
    ItemStack getDisplayedItem();

    /**
     * Sets the item to display inside this vault until the next cycle.
     *
     * @param displayedItem The item to display
     */
    void setDisplayedItem(ItemStack displayedItem);
}
