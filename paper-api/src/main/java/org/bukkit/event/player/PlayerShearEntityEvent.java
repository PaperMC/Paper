package org.bukkit.event.player;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a player shears an entity
 */
public class PlayerShearEntityEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancel;
    private final Entity what;
    private final ItemStack item;
    private final EquipmentSlot hand;
    private java.util.List<ItemStack> drops; // Paper - custom shear drops

    @org.jetbrains.annotations.ApiStatus.Internal // Paper
    public PlayerShearEntityEvent(@NotNull Player who, @NotNull Entity what, @NotNull ItemStack item, @NotNull EquipmentSlot hand, final java.util.@NotNull List<ItemStack> drops) { // Paper - custom shear drops
        super(who);
        this.what = what;
        this.item = item;
        this.hand = hand;
        this.drops = drops; // Paper - custom shear drops
    }

    @Deprecated(since = "1.15.2")
    public PlayerShearEntityEvent(@NotNull final Player who, @NotNull final Entity what) {
        this(who, what, new ItemStack(Material.SHEARS), EquipmentSlot.HAND, java.util.Collections.emptyList()); // Paper - custom shear drops
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    /**
     * Gets the entity the player is shearing
     *
     * @return the entity the player is shearing
     */
    @NotNull
    public Entity getEntity() {
        return what;
    }

    /**
     * Gets the item used to shear the entity.
     *
     * @return the shears
     */
    @NotNull
    public ItemStack getItem() {
        return item.clone();
    }

    /**
     * Gets the hand used to shear the entity.
     *
     * @return the hand
     */
    @NotNull
    public EquipmentSlot getHand() {
        return hand;
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

    // Paper start - custom shear drops
    /**
     * Get an immutable list of drops for this shearing.
     *
     * @return the shearing drops
     * @see #setDrops(java.util.List)
     */
    public java.util.@NotNull @org.jetbrains.annotations.Unmodifiable List<ItemStack> getDrops() {
        return this.drops;
    }

    /**
     * Sets the drops for the shearing.
     *
     * @param drops the shear drops
     */
    public void setDrops(final java.util.@NotNull List<org.bukkit.inventory.ItemStack> drops) {
        this.drops = java.util.List.copyOf(drops);
    }
    // Paper end - custom shear drops
}
