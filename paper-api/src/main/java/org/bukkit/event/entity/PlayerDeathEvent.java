package org.bukkit.event.entity;

import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Thrown whenever a {@link Player} dies
 */
public class PlayerDeathEvent extends EntityDeathEvent {
    private int newExp = 0;
    private boolean displayDeathMessage;
    private Component deathChatMessage;
    private Component deathScreenMessage;
    private int newLevel = 0;
    private int newTotalExp = 0;
    private boolean keepLevel = false;
    private boolean keepInventory = false;
    private boolean doExpDrop;

    @org.jetbrains.annotations.ApiStatus.Internal
    public PlayerDeathEvent(final @NotNull Player player, final @NotNull DamageSource damageSource, final @NotNull List<ItemStack> drops, final int droppedExp, final int newExp, final boolean displayDeathMessage, final @Nullable Component deathMessage) {
        this(player, damageSource, drops, droppedExp, newExp, 0, 0, displayDeathMessage, deathMessage);
    }

    @org.jetbrains.annotations.ApiStatus.Internal
    public PlayerDeathEvent(final @NotNull Player player, final @NotNull DamageSource damageSource, final @NotNull List<ItemStack> drops, final int droppedExp, final int newExp, final int newTotalExp, final int newLevel, final boolean displayDeathMessage, final @Nullable Component deathMessage) {
        this(player, damageSource, drops, droppedExp, newExp, newTotalExp, newLevel, displayDeathMessage, deathMessage, true);
    }
    @org.jetbrains.annotations.ApiStatus.Internal
    public PlayerDeathEvent(final @NotNull Player player, final @NotNull DamageSource damageSource, final @NotNull List<ItemStack> drops, final int droppedExp, final int newExp, final int newTotalExp, final int newLevel, final boolean displayDeathMessage, final @Nullable Component deathMessage, final boolean doExpDrop) {
        super(player, damageSource, drops, droppedExp);
        this.newExp = newExp;
        this.newTotalExp = newTotalExp;
        this.newLevel = newLevel;
        this.displayDeathMessage = displayDeathMessage;
        this.deathChatMessage = deathMessage;
        this.deathScreenMessage = deathMessage;
        this.doExpDrop = doExpDrop;
    }

    @Deprecated
    public PlayerDeathEvent(@NotNull final Player player, @NotNull DamageSource damageSource, @NotNull final List<ItemStack> drops, final int droppedExp, @Nullable final String deathMessage) {
        this(player, damageSource, drops, droppedExp, 0, deathMessage);
    }

    @Deprecated
    public PlayerDeathEvent(@NotNull final Player player, @NotNull DamageSource damageSource, @NotNull final List<ItemStack> drops, final int droppedExp, final int newExp, @Nullable final String deathMessage) {
        this(player, damageSource, drops, droppedExp, newExp, 0, 0, deathMessage);
    }

    @Deprecated
    public PlayerDeathEvent(@NotNull final Player player, @NotNull DamageSource damageSource, @NotNull final List<ItemStack> drops, final int droppedExp, final int newExp, final int newTotalExp, final int newLevel, @Nullable final String deathMessage) {
        this(player, damageSource, drops, droppedExp, newExp, newTotalExp, newLevel, deathMessage, true);
    }

    @Deprecated
    public PlayerDeathEvent(@NotNull final Player player, final @NotNull DamageSource damageSource, @NotNull final List<ItemStack> drops, final int droppedExp, final int newExp, final int newTotalExp, final int newLevel, @Nullable final String deathMessage, boolean doExpDrop) {
        super(player, damageSource, drops, droppedExp);
        this.newExp = newExp;
        this.newTotalExp = newTotalExp;
        this.newLevel = newLevel;
        this.displayDeathMessage = true;
        this.deathChatMessage = LegacyComponentSerializer.legacySection().deserializeOrNull(deathMessage);
        this.deathScreenMessage = deathChatMessage;
        this.doExpDrop = doExpDrop;
    }

    @Deprecated
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
     */
    @NotNull
    public List<ItemStack> getItemsToKeep() {
        return itemsToKeep;
    }

    /**
     * @return should experience be dropped from this death
     */
    public boolean shouldDropExperience() {
        return doExpDrop;
    }

    /**
     * @param doExpDrop sets if experience should be dropped from this death
     */
    public void setShouldDropExperience(boolean doExpDrop) {
        this.doExpDrop = doExpDrop;
    }

    @NotNull
    @Override
    public Player getEntity() {
        return (Player) entity;
    }

    /**
     * Clarity method for getting the player. Not really needed except
     * for reasons of clarity.
     *
     * @return Player who is involved in this event
     */
    public @NotNull Player getPlayer() {
        return this.getEntity();
    }

    /**
     * Get whether the death message should be shown.
     * By default, this is determined by {@link org.bukkit.GameRule#SHOW_DEATH_MESSAGES}.
     *
     * @return whether the death message should be shown
     * @see #deathChatMessage()
     * @see #deathScreenMessage()
     */
    public boolean allowDisplayingDeathMessage() {
        return displayDeathMessage;
    }

    /**
     * Set whether the death message should be shown.
     * By default, this is determined by {@link org.bukkit.GameRule#SHOW_DEATH_MESSAGES}.
     *
     * @param displayDeathMessage whether the death message should be shown
     * @see #deathChatMessage()
     * @see #deathScreenMessage()
     */
    public void allowDisplayingDeathMessage(boolean displayDeathMessage) {
        this.displayDeathMessage = displayDeathMessage;
    }

    /**
     * Set the death message that will be broadcasted and appear on the players death screen.
     *
     * @param deathMessage Component message to appear to other players on the server.
     */
    public void deathMessage(final @Nullable Component deathMessage) {
        this.deathChatMessage = deathMessage;
        this.deathScreenMessage = deathMessage;
    }

    /**
     * Get the death message that will appear to everyone on the server.
     *
     * @return Component message to appear to other players on the server.
     * @deprecated in favour of {@link #deathChatMessage()} and {@link #deathScreenMessage()}
     */
    @Deprecated
    public @Nullable Component deathMessage() {
        return this.deathChatMessage;
    }

    /**
     * Set the death message that will be broadcasted and appear on the players death screen.
     *
     * @param deathMessage message to appear to other players on the server.
     * @deprecated in favour of {@link #deathMessage(Component)}
     */
    @Deprecated
    public void setDeathMessage(@Nullable String deathMessage) {
        this.deathChatMessage = this.deathScreenMessage = LegacyComponentSerializer.legacySection().deserializeOrNull(deathMessage);
    }

    /**
     * Get the death message that will appear to everyone on the server.
     *
     * @return Message to appear to other players on the server.
     * @deprecated in favour of {@link #deathChatMessage()} and {@link #deathScreenMessage()}
     */
    @Nullable
    @Deprecated
    public String getDeathMessage() {
        return LegacyComponentSerializer.legacySection().serializeOrNull(this.deathChatMessage);
    }

    /**
     * Set the death message that will appear in the chat.
     * <p>
     * If set to null, no message will be sent to chat.
     *
     * @param deathChatMessage Message to appear in the chat when the player dies.
     */
    public void deathChatMessage(@Nullable Component deathChatMessage) {
        this.deathChatMessage = deathChatMessage;
    }

    /**
     * Get the death message that will appear in the chat.
     *
     * @return Message to appear in chat.
     */
    public @Nullable Component deathChatMessage() {
        return this.deathChatMessage;
    }
    /**
     * Set the death message that will appear on the death screen of the dying player.
     * <p>
     * If set to null, no message will be shown to the dying player.
     * <p>
     * If the message exceeds 256 characters it will be truncated.
     *
     * @param deathScreenMessage Message to appear on the death screen to the dying player.
     */
    public void deathScreenMessage(@Nullable Component deathScreenMessage) {
        this.deathScreenMessage = deathScreenMessage;
    }

    /**
     * Get the death message that will appear on the death screen of the dying player.
     * By default, it is the same value as {@link #deathChatMessage()}.
     *
     * @return Message to appear on the death screen to the dying player.
     */
    public @Nullable Component deathScreenMessage() {
        return this.deathScreenMessage;
    }

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
     */
    public int getNewLevel() {
        return newLevel;
    }

    /**
     * Sets the Level the Player should have at respawn.
     *
     * @param level New Level of the respawned player
     */
    public void setNewLevel(int level) {
        newLevel = level;
    }

    /**
     * Gets the Total EXP the Player should have at respawn.
     *
     * @return New Total EXP of the respawned player
     */
    public int getNewTotalExp() {
        return newTotalExp;
    }

    /**
     * Sets the Total EXP the Player should have at respawn.
     *
     * @param totalExp New Total EXP of the respawned player
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
     */
    public void setKeepInventory(boolean keepInventory) {
        this.keepInventory = keepInventory;
    }

    /**
     * Gets if the Player keeps inventory on death.
     *
     * @return True if the player keeps inventory on death
     */
    public boolean getKeepInventory() {
        return keepInventory;
    }
}
