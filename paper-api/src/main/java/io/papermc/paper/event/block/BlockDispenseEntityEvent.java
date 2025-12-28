package io.papermc.paper.event.block;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Called when an entity is spawned by a dispenser
 * <p>
 * If this event is canceled, the entity will not
 * spawn
 */
public class BlockDispenseEntityEvent extends BlockDispenseEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Entity entity;

    @ApiStatus.Internal
    public BlockDispenseEntityEvent(final @NotNull Block block, final @NotNull ItemStack item, final @NotNull Vector velocity, final @NotNull Entity entity) {
        super(block, item, velocity);
        this.entity = entity;
    }

    /**
     * @return The entity that's dispensed
     *
     */
    @NotNull
    public Entity getEntity() {
        return this.entity;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

}
