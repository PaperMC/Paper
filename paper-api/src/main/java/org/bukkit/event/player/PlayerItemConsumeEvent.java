package org.bukkit.event.player;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This event will fire when a player is finishing consuming an item (food,
 * potion, milk bucket).
 * <br>
 * If the ItemStack is modified the server will use the effects of the new
 * item and not remove the original one from the player's inventory.
 * <br>
 * If the event is cancelled the effect will not be applied and the item will
 * not be removed from the player's inventory.
 */
public class PlayerItemConsumeEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean isCancelled = false;
    private ItemStack item;
    private final EquipmentSlot hand;
    @Nullable private ItemStack replacement; // Paper

    /**
     * @param player the player consuming
     * @param item the ItemStack being consumed
     * @param hand the hand that was used
     */
    @ApiStatus.Internal
    public PlayerItemConsumeEvent(@NotNull final Player player, @NotNull final ItemStack item, @NotNull final EquipmentSlot hand) {
        super(player);

        this.item = item;
        this.hand = hand;
    }

    /**
     * @param player the player consuming
     * @param item the ItemStack being consumed
     * @deprecated use {@link #PlayerItemConsumeEvent(Player, ItemStack, EquipmentSlot)}
     */
    @Deprecated(since = "1.19.2", forRemoval = true)
    public PlayerItemConsumeEvent(@NotNull final Player player, @NotNull final ItemStack item) {
        this(player, item, EquipmentSlot.HAND);
    }

    /**
     * Gets the item that is being consumed. Modifying the returned item will
     * have no effect, you must use {@link
     * #setItem(org.bukkit.inventory.ItemStack)} instead.
     *
     * @return an ItemStack for the item being consumed
     */
    @NotNull
    public ItemStack getItem() {
        return item.clone();
    }

    /**
     * Set the item being consumed
     *
     * @param item the item being consumed
     */
    public void setItem(@Nullable ItemStack item) {
        if (item == null) {
            this.item = new ItemStack(Material.AIR);
        } else {
            this.item = item;
        }
    }

    /**
     * Get the hand used to consume the item.
     *
     * @return the hand
     */
    @NotNull
    public EquipmentSlot getHand() {
        return hand;
    }

    // Paper start
    /**
     * Return the custom item stack that will replace the consumed item, or null if no
     * custom replacement has been set (which means the default replacement will be used).
     *
     * @return The custom item stack that will replace the consumed item or null
     */
    @Nullable
    public ItemStack getReplacement() {
        return this.replacement;
    }

    /**
     * Set a custom item stack to replace the consumed item. Pass null to clear any custom
     * stack that has been set and use the default replacement.
     *
     * @param replacement Replacement item to set, null to clear any custom stack and use default
     */
    public void setReplacement(@Nullable ItemStack replacement) {
        this.replacement = replacement;
    }
    // Paper end

    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.isCancelled = cancel;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
