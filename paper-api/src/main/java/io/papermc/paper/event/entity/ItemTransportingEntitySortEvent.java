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
    private boolean overrideDefaultBehaviour = false;
    private boolean itemBelongs = true;

    @ApiStatus.Internal
    public ItemTransportingEntitySortEvent(
        final Entity entity,
        final ItemStack itemStack,
        final Inventory containerInventory
    ) {
        super(entity);
        this.containerInventory = containerInventory;
        this.itemStack = itemStack;
    }

    /**
     * Sets if the item belongs in the container.
     *
     * @param belongs Whether the item belongs or not
     */
    public void setItemBelongs(boolean belongs) {
        this.overrideDefaultBehaviour = true;
        this.itemBelongs = belongs;
    }

    /**
     * Gets if the held item stack belongs in the container.
     *
     * @return Whether the item stack belongs.
     */
    public boolean itemBelongs() {
        return this.itemBelongs;
    }

    /**
     * Whether this event will override the default behavior of the entity.
     * For example, if {@link ItemTransportingEntitySortEvent#setItemBelongs(boolean)} is not called, the entity
     * will use the default behavior of adding to empty chests and chests containing a matching item stack.
     * Once the function is called, the entity will defer to the plugin's decision.
     *
     * @return Whether the default behavior is being overridden.
     */
    public boolean isOverridingDefaultBehaviour() {
        return this.overrideDefaultBehaviour;
    }

    /**
     * Gets the container the entity is comparing to the held item.
     *
     * @return The potential destination container
     */
    public Inventory getContainerInventory() {
        return this.containerInventory;
    }

    /**
     * Gets the held item stack being compared against the container.
     *
     * @return The held item stack
     */
    public ItemStack getHeldItemStack() {
        return this.itemStack;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
