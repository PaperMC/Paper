package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Event fired when a dispenser shears a nearby sheep.
 */
public class BlockShearEntityEvent extends BlockEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    //
    private final Entity sheared;
    private final ItemStack tool;
    private boolean cancelled;
    private java.util.List<ItemStack> drops; // Paper

    @org.jetbrains.annotations.ApiStatus.Internal // Paper
    public BlockShearEntityEvent(@NotNull Block dispenser, @NotNull Entity sheared, @NotNull ItemStack tool, final @NotNull java.util.List<ItemStack> drops) { // Paper - custom shear drops
        super(dispenser);
        this.sheared = sheared;
        this.tool = tool;
        this.drops = drops; // Paper
    }

    /**
     * Gets the entity that was sheared.
     *
     * @return the entity that was sheared.
     */
    @NotNull
    public Entity getEntity() {
        return sheared;
    }

    /**
     * Gets the item used to shear this sheep.
     *
     * @return the item used to shear this sheep.
     */
    @NotNull
    public ItemStack getTool() {
        return tool.clone();
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
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
        return java.util.Collections.unmodifiableList(this.drops);
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
