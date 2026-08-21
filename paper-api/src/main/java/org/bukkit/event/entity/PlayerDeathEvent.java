package org.bukkit.event.entity;

import java.util.List;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.Nullable;

/**
 * Thrown whenever a {@link Player} dies
 */
public interface PlayerDeathEvent extends EntityDeathEvent {

    @Override
    Player getEntity();

    /**
     * Get whether the death message should be shown.
     * By default, this is determined by {@link org.bukkit.GameRules#SHOW_DEATH_MESSAGES}.
     *
     * @return whether the death message should be shown
     * @see #deathMessage()
     * @see #deathScreenMessageOverride()
     */
    boolean getShowDeathMessages();

    /**
     * Set whether the death message should be shown.
     * By default, this is determined by {@link org.bukkit.GameRules#SHOW_DEATH_MESSAGES}.
     *
     * @param displayDeathMessage whether the death message should be shown
     * @see #deathMessage()
     * @see #deathScreenMessageOverride()
     */
    void setShowDeathMessages(boolean displayDeathMessage);

    /**
     * Clarity method for getting the player. Not really needed except
     * for reasons of clarity.
     *
     * @return Player who is involved in this event
     */
    default Player getPlayer() {
        return this.getEntity();
    }

    /**
     * Gets how much EXP the Player should have at respawn.
     * <p>
     * This does not indicate how much EXP should be dropped, please see
     * {@link #getDroppedExp()} for that.
     *
     * @return New EXP of the respawned player
     */
    int getNewExp();

    /**
     * Sets how much EXP the Player should have at respawn.
     * <p>
     * This does not indicate how much EXP should be dropped, please see
     * {@link #setDroppedExp(int)} for that.
     *
     * @param exp New EXP of the respawned player
     */
    void setNewExp(int exp);

    /**
     * Gets the Level the Player should have at respawn.
     *
     * @return New Level of the respawned player
     */
    int getNewLevel();

    /**
     * Sets the Level the Player should have at respawn.
     *
     * @param level New Level of the respawned player
     */
    void setNewLevel(int level);

    /**
     * Gets the Total EXP the Player should have at respawn.
     *
     * @return New Total EXP of the respawned player
     */
    int getNewTotalExp();

    /**
     * Sets the Total EXP the Player should have at respawn.
     *
     * @param totalExp New Total EXP of the respawned player
     */
    void setNewTotalExp(int totalExp);

    /**
     * Get the death message that will appear to everyone on the server.
     *
     * @return Component message to appear to other players on the server.
     */
    @Nullable Component deathMessage();

    /**
     * Set the death message that will appear to everyone on the server.
     *
     * @param deathMessage Component message to appear to other players on the server.
     */
    void deathMessage(@Nullable Component deathMessage);

    /**
     * Get the death message that will appear to everyone on the server.
     *
     * @return Message to appear to other players on the server.
     * @deprecated in favour of {@link #deathMessage()}
     */
    @Deprecated
    @Nullable String getDeathMessage();

    /**
     * Set the death message that will appear to everyone on the server.
     *
     * @param deathMessage message to appear to other players on the server.
     * @deprecated in favour of {@link #deathMessage(Component)}
     */
    @Deprecated
    void setDeathMessage(@Nullable String deathMessage);

    /**
     * Get the death message override that will appear on the death screen of the dying player.
     * By default, this is null.
     * <p>
     * If set to null, death screen message will be same as {@code deathMessage()}.
     * <p>
     * @return Message to appear on the death screen to the dying player.
     */
    @Nullable Component deathScreenMessageOverride();

    /**
     * Overrides the death message that will appear on the death screen of the dying player.
     * By default, this is null.
     * <p>
     * If set to null, death screen message will be same as {@code deathMessage()}.
     * <p>
     * If the message exceeds 256 characters it will be truncated.
     *
     * @param deathScreenMessageOverride Message to appear on the death screen to the dying player.
     */
    void deathScreenMessageOverride(@Nullable Component deathScreenMessageOverride);

    /**
     * @return should experience be dropped from this death
     */
    boolean shouldDropExperience();

    /**
     * @param doExpDrop sets if experience should be dropped from this death
     */
    void setShouldDropExperience(boolean doExpDrop);

    /**
     * Gets if the Player should keep all EXP at respawn.
     * <p>
     * This flag overrides other EXP settings
     *
     * @return {@code true} if Player should keep all pre-death exp
     */
    boolean getKeepLevel();

    /**
     * Sets if the Player should keep all EXP at respawn.
     * <p>
     * This overrides all other EXP settings
     * <p>
     * <b>This doesn't prevent the EXP from dropping.
     * {@link #setDroppedExp(int)} should be used stop the
     * EXP from dropping.</b>
     *
     * @param keepLevel {@code true} to keep all current value levels
     */
    void setKeepLevel(boolean keepLevel);

    /**
     * Gets if the Player keeps inventory on death.
     *
     * @return {@code true} if the player keeps inventory on death
     */
    boolean getKeepInventory();

    /**
     * Sets if the Player keeps inventory on death.
     * <p>
     * <b>This doesn't prevent the items from dropping.
     * {@code getDrops().clear()} should be used stop the
     * items from dropping.</b>
     *
     * @param keepInventory {@code true} to keep the inventory
     */
    void setKeepInventory(boolean keepInventory);

    /**
     * A mutable collection to add items that the player should retain in their inventory on death (Similar to KeepInventory game rule)
     * <br>
     * You <b>MUST</b> remove the item from the {@link #getDrops()} collection too, or it will duplicate!
     * <pre>{@code
     * private static final NamespacedKey SOULBOUND_KEY = new NamespacedKey("testplugin", "soulbound");
     *
     * @EventHandler(ignoreCancelled = true)
     * public void onPlayerDeath(PlayerDeathEvent event) {
     *     for (Iterator<ItemStack> iterator = event.getDrops().iterator(); iterator.hasNext(); ) {
     *         ItemStack drop = iterator.next();
     *         if (drop.getPersistentDataContainer().getOrDefault(SOULBOUND_KEY, PersistentDataType.BOOLEAN, false)) {
     *             iterator.remove();
     *             event.getItemsToKeep().add(drop);
     *         }
     *     }
     * }
     * }</pre>
     * <p>
     * Adding an item to this list that the player did not previously have will give them the item on death.
     * An example case could be a "Note" that "You died at X/Y/Z coordinates"
     *
     * @return The list to hold items to keep
     */
    List<ItemStack> getItemsToKeep();
}
