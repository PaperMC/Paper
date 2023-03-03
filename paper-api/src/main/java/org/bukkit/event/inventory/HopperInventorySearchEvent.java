package org.bukkit.event.inventory;

import org.bukkit.block.Block;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Event that gets called each time a Hopper attempts to find its
 * source/attached containers.
 */
public class HopperInventorySearchEvent extends BlockEvent {

    private static final HandlerList handlers = new HandlerList();
    private Inventory inventory;
    private final ContainerType containerType;
    private final Block searchBlock;

    public enum ContainerType {

        /**
         * The source container the hopper is looking for.
         *
         * This is the Inventory above the Hopper where it extracts items from.
         */
        SOURCE,
        /**
         * The container the hopper is attached to.
         *
         * This is the Inventory the Hopper pushes items into.
         */
        DESTINATION;
    }

    public HopperInventorySearchEvent(@NotNull Inventory inventory, @NotNull ContainerType containerType, @NotNull Block hopper, @NotNull Block searchBlock) {
        super(hopper);
        this.inventory = inventory;
        this.containerType = containerType;
        this.searchBlock = searchBlock;
    }

    /**
     * Set the {@link Inventory} that the Hopper will use for its
     * source/attached Container.
     *
     * @param inventory the inventory to use
     */
    public void setInventory(@Nullable Inventory inventory) {
        this.inventory = inventory;
    }

    /**
     * Gets the {@link Inventory} that the Hopper will use for its
     * source/attached Container.
     *
     * @return the inventory which will be used
     */
    @Nullable
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Gets the Container type the Hopper is searching for.
     *
     * @return the container type being searched for
     */
    @NotNull
    public ContainerType getContainerType() {
        return containerType;
    }

    /**
     * Gets the Block that is being searched for an inventory.
     *
     * @return block being searched for an inventory
     */
    @NotNull
    public Block getSearchBlock() {
        return searchBlock;
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
