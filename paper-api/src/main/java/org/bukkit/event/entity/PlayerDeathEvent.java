package org.bukkit.event.entity;

import java.util.List;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Thrown whenever a {@link Player} dies
 *
 * @since 1.0.0
 */
public class PlayerDeathEvent extends EntityDeathEvent {
    private int newExp = 0;
    private net.kyori.adventure.text.Component deathMessage; // Paper - adventure
    private int newLevel = 0;
    private int newTotalExp = 0;
    private boolean keepLevel = false;
    private boolean keepInventory = false;
    private boolean doExpDrop; // Paper - shouldDropExperience API
    // Paper start - adventure
    @org.jetbrains.annotations.ApiStatus.Internal
    public PlayerDeathEvent(final @NotNull Player player, final @NotNull DamageSource damageSource, final @NotNull List<ItemStack> drops, final int droppedExp, final @Nullable net.kyori.adventure.text.Component deathMessage) {
        this(player, damageSource, drops, droppedExp, 0, deathMessage);
    }

    @org.jetbrains.annotations.ApiStatus.Internal
    public PlayerDeathEvent(final @NotNull Player player, final @NotNull DamageSource damageSource, final @NotNull List<ItemStack> drops, final int droppedExp, final int newExp, final @Nullable net.kyori.adventure.text.Component deathMessage) {
        this(player, damageSource, drops, droppedExp, newExp, 0, 0, deathMessage);
    }

    @org.jetbrains.annotations.ApiStatus.Internal
    public PlayerDeathEvent(final @NotNull Player player, final @NotNull DamageSource damageSource, final @NotNull List<ItemStack> drops, final int droppedExp, final int newExp, final int newTotalExp, final int newLevel, final @Nullable net.kyori.adventure.text.Component deathMessage) {
        // Paper start - shouldDropExperience API
        this(player, damageSource, drops, droppedExp, newExp, newTotalExp, newLevel, deathMessage, true);
    }
    @org.jetbrains.annotations.ApiStatus.Internal
    public PlayerDeathEvent(final @NotNull Player player, final @NotNull DamageSource damageSource, final @NotNull List<ItemStack> drops, final int droppedExp, final int newExp, final int newTotalExp, final int newLevel, final @Nullable net.kyori.adventure.text.Component deathMessage, final boolean doExpDrop) {
        // Paper end - shouldDropExperience API
        super(player, damageSource, drops, droppedExp);
        this.newExp = newExp;
        this.newTotalExp = newTotalExp;
        this.newLevel = newLevel;
        this.deathMessage = deathMessage;
        this.doExpDrop = doExpDrop; // Paper - shouldDropExperience API
    }
    // Paper end - adventure

    @Deprecated // Paper
    public PlayerDeathEvent(@NotNull final Player player, @NotNull DamageSource damageSource, @NotNull final List<ItemStack> drops, final int droppedExp, @Nullable final String deathMessage) {
        this(player, damageSource, drops, droppedExp, 0, deathMessage);
    }

    @Deprecated // Paper
    public PlayerDeathEvent(@NotNull final Player player, @NotNull DamageSource damageSource, @NotNull final List<ItemStack> drops, final int droppedExp, final int newExp, @Nullable final String deathMessage) {
        this(player, damageSource, drops, droppedExp, newExp, 0, 0, deathMessage);
    }

    @Deprecated // Paper
    public PlayerDeathEvent(@NotNull final Player player, @NotNull DamageSource damageSource, @NotNull final List<ItemStack> drops, final int droppedExp, final int newExp, final int newTotalExp, final int newLevel, @Nullable final String deathMessage) {
        // Paper start - shouldDropExperience API
        this(player, damageSource, drops, droppedExp, newExp, newTotalExp, newLevel, deathMessage, true);
    }

    @Deprecated // Paper
    public PlayerDeathEvent(@NotNull final Player player, final @NotNull DamageSource damageSource, @NotNull final List<ItemStack> drops, final int droppedExp, final int newExp, final int newTotalExp, final int newLevel, @Nullable final String deathMessage, boolean doExpDrop) {
        // Paper end - shouldDropExperience API
        super(player, damageSource, drops, droppedExp);
        this.newExp = newExp;
        this.newTotalExp = newTotalExp;
        this.newLevel = newLevel;
        this.deathMessage = net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().deserializeOrNull(deathMessage); // Paper
        this.doExpDrop = doExpDrop; // Paper - shouldDropExperience API
    }

    @Deprecated // Paper
    // Paper start
    private List<ItemStack> itemsToKeep = new java.util.ArrayList<>();

    /**
     * A mutable collection to add items that the player should retain in their inventory on death (Similar to KeepInventory game rule)
     *
     * You <b>MUST</b> remove the item from the .getDrops() collection too or it will duplicate!
     * <pre>{@code
     *    {@literal @EventHandler(ignoreCancelled = true)}
     *     public void onPlayerDeath(PlayerDeathEvent event) {
     *         for (Iterator<ItemStack> iterator = event.getDrops().iterator(); iterator.hasNext(); ) {
     *             ItemStack drop = iterator.next();
     *             List<String> lore = drop.getLore();
     *             if (lore != null && !lore.isEmpty()) {
     *                 if (lore.get(0).contains("(SOULBOUND)")) {
     *                     iterator.remove();
     *                     event.getItemsToKeep().add(drop);
     *                 }
     *             }
     *         }
     *     }
     * }</pre>
     *
     * Adding an item to this list that the player did not previously have will give them the item on death.
     * An example case could be a "Note" that "You died at X/Y/Z coordinates"
     *
     * @return The list to hold items to keep
     * @since 1.13.2
     */
    @NotNull
    public List<ItemStack> getItemsToKeep() {
        return itemsToKeep;
    }
    // Paper end

    // Paper start - shouldDropExperience API
    /**
     * @return should experience be dropped from this death
     * @since 1.15.1
     */
    public boolean shouldDropExperience() {
        return doExpDrop;
    }

    /**
     * @param doExpDrop sets if experience should be dropped from this death
     * @since 1.15.1
     */
    public void setShouldDropExperience(boolean doExpDrop) {
        this.doExpDrop = doExpDrop;
    }
    // Paper end - shouldDropExperience API

    /**
     * @since 1.1.0
     */
    @NotNull
    @Override
    public Player getEntity() {
        return (Player) entity;
    }

    // Paper start - improve death events
    /**
     * Clarity method for getting the player. Not really needed except
     * for reasons of clarity.
     *
     * @return Player who is involved in this event
     * @since 1.17.1
     */
    public @NotNull Player getPlayer() {
        return this.getEntity();
    }

    // Paper end - improve death events

    // Paper start - adventure
    /**
     * Set the death message that will appear to everyone on the server.
     *
     * @param deathMessage Component message to appear to other players on the server.
     * @since 1.16.5
     */
    public void deathMessage(final net.kyori.adventure.text.@Nullable Component deathMessage) {
        this.deathMessage = deathMessage;
    }

    /**
     * Get the death message that will appear to everyone on the server.
     *
     * @return Component message to appear to other players on the server.
     * @since 1.16.5
     */
    public net.kyori.adventure.text.@Nullable Component deathMessage() {
        return this.deathMessage;
    }
    // Paper end - adventure

    /**
     * Set the death message that will appear to everyone on the server.
     *
     * @param deathMessage Message to appear to other players on the server.
     * @deprecated in favour of {@link #deathMessage(net.kyori.adventure.text.Component)}
     */
    @Deprecated // Paper
    public void setDeathMessage(@Nullable String deathMessage) {
        this.deathMessage = net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().deserializeOrNull(deathMessage); // Paper
    }

    /**
     * Get the death message that will appear to everyone on the server.
     *
     * @return Message to appear to other players on the server.
     * @deprecated in favour of {@link #deathMessage()}
     */
    @Nullable
    @Deprecated // Paper
    public String getDeathMessage() {
        return net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().serializeOrNull(this.deathMessage); // Paper
    }
    // Paper end
    /**
     * Gets how much EXP the Player should have at respawn.
     * <p>
     * This does not indicate how much EXP should be dropped, please see
     * {@link #getDroppedExp()} for that.
     *
     * @return New EXP of the respawned player
     */
    public int getNewExp() {
        return newExp;
    }

    /**
     * Sets how much EXP the Player should have at respawn.
     * <p>
     * This does not indicate how much EXP should be dropped, please see
     * {@link #setDroppedExp(int)} for that.
     *
     * @param exp New EXP of the respawned player
     */
    public void setNewExp(int exp) {
        newExp = exp;
    }

    /**
     * Gets the Level the Player should have at respawn.
     *
     * @return New Level of the respawned player
     * @since 1.1.0
     */
    public int getNewLevel() {
        return newLevel;
    }

    /**
     * Sets the Level the Player should have at respawn.
     *
     * @param level New Level of the respawned player
     * @since 1.1.0
     */
    public void setNewLevel(int level) {
        newLevel = level;
    }

    /**
     * Gets the Total EXP the Player should have at respawn.
     *
     * @return New Total EXP of the respawned player
     * @since 1.1.0
     */
    public int getNewTotalExp() {
        return newTotalExp;
    }

    /**
     * Sets the Total EXP the Player should have at respawn.
     *
     * @param totalExp New Total EXP of the respawned player
     * @since 1.1.0
     */
    public void setNewTotalExp(int totalExp) {
        newTotalExp = totalExp;
    }

    /**
     * Gets if the Player should keep all EXP at respawn.
     * <p>
     * This flag overrides other EXP settings
     *
     * @return True if Player should keep all pre-death exp
     * @since 1.1.0
     */
    public boolean getKeepLevel() {
        return keepLevel;
    }

    /**
     * Sets if the Player should keep all EXP at respawn.
     * <p>
     * This overrides all other EXP settings
     * <p>
     * <b>This doesn't prevent the EXP from dropping.
     * {@link #setDroppedExp(int)} should be used stop the
     * EXP from dropping.</b>
     *
     * @param keepLevel True to keep all current value levels
     * @since 1.1.0
     */
    public void setKeepLevel(boolean keepLevel) {
        this.keepLevel = keepLevel;
    }

    /**
     * Sets if the Player keeps inventory on death.
     * <p>
     * <b>This doesn't prevent the items from dropping.
     * {@code getDrops().clear()} should be used stop the
     * items from dropping.</b>
     *
     * @param keepInventory True to keep the inventory
     * @since 1.7.10
     */
    public void setKeepInventory(boolean keepInventory) {
        this.keepInventory = keepInventory;
    }

    /**
     * Gets if the Player keeps inventory on death.
     *
     * @return True if the player keeps inventory on death
     * @since 1.7.10
     */
    public boolean getKeepInventory() {
        return keepInventory;
    }
}
