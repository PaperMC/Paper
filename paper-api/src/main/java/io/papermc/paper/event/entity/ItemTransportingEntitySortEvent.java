package io.papermc.paper.event.entity;

import org.bukkit.block.Container;
import org.bukkit.entity.CopperGolem;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Called when an item-transporting entity (typically a {@link CopperGolem},
 * although other entities may be possible through non-API means)
 * is inspecting a destination {@link Container} to decide if the item it is holding
 * belongs in said container. This gives plugin developers an opportunity to
 * override the {@link CopperGolem}'s sorting behavior.
 */
@NullMarked
public class ItemTransportingEntitySortEvent extends EntityEvent {
    protected static final HandlerList HANDLER_LIST = new HandlerList();

    private final ItemStack itemStack;
    private final Inventory containerInventory;
    private Result result;

    @ApiStatus.Internal
    public ItemTransportingEntitySortEvent(
        final Entity entity,
        final ItemStack itemStack,
        final Inventory containerInventory
    ) {
        super(entity);
        this.containerInventory = containerInventory;
        this.itemStack = itemStack;
        this.result = Result.DEFAULT;
    }

    /**
     * Sets the sorting decision for the held item and the inspected container.
     * <ul>
     *   <li>{@link Result#ALLOW}: the item belongs in the container.</li>
     *   <li>{@link Result#DENY}: the item does not belong in the container.</li>
     *   <li>{@link Result#DEFAULT}: use the vanilla sorting logic.</li>
     * </ul>
     *
     * @param result the sorting decision
     * @see Result
     */
    public void setResult(final Result result) {
        this.result = result;
    }

    /**
     * Gets the current sorting decision. Starts as {@link Result#DEFAULT}
     * and may have been modified by other plugins.
     *
     * @return the current sorting decision
     * @see #setResult(Result)
     */
    public Result getResult() {
        return this.result;
    }

    /**
     * Gets the inventory of the container the entity is comparing
     * against the held item.
     *
     * @return the inventory of the potential destination container
     */
    public Inventory getContainerInventory() {
        return this.containerInventory;
    }

    /**
     * Gets the item stack held by the entity that is being compared
     * against the container.
     *
     * @return the held item stack
     */
    public ItemStack getItemStack() {
        return this.itemStack;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
