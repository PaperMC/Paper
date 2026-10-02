package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import java.util.Collections;
import java.util.List;

/**
 * Event fired when a dispenser shears a nearby entity.
 */
public class BlockShearEntityEvent extends BlockEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Entity sheared;
    private final ItemStack tool;
    private List<ItemStack> drops;

    private boolean cancelled;

    @ApiStatus.Internal
    public BlockShearEntityEvent(@NotNull Block dispenser, @NotNull Entity sheared, @NotNull ItemStack tool, final @NotNull List<ItemStack> drops) {
        super(dispenser);
        this.sheared = sheared;
        this.tool = tool;
        this.drops = drops;
    }

    /**
     * Gets the entity that was sheared.
     *
     * @return the entity that was sheared.
     */
    @NotNull
    public Entity getEntity() {
        return this.sheared;
    }

    /**
     * Gets the item used to shear this entity.
     *
     * @return the item used to shear this entity.
     */
    @NotNull
    public ItemStack getTool() {
        return this.tool.clone();
    }

    /**
     * Get an immutable list of drops for this shearing.
     *
     * @return the shearing drops
     * @see #setDrops(List)
     */
    public @NotNull @Unmodifiable List<ItemStack> getDrops() {
        return Collections.unmodifiableList(this.drops);
    }

    /**
     * Sets the drops for the shearing.
     *
     * @param drops the shear drops
     */
    public void setDrops(final @NotNull List<ItemStack> drops) {
        this.drops = List.copyOf(drops);
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
