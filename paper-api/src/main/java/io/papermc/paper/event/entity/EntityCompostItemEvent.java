package io.papermc.paper.event.entity;

import io.papermc.paper.event.block.CompostItemEvent;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Called when an item is about to be composted by an entity.
 */
@NullMarked
public class EntityCompostItemEvent extends CompostItemEvent implements Cancellable {

    private final Entity entity;
    private boolean cancelled;

    @ApiStatus.Internal
    public EntityCompostItemEvent(final Entity entity, final Block composter, final ItemStack item, final boolean willRaiseLevel) {
        super(composter, item, willRaiseLevel);
        this.entity = entity;
    }

    /**
     * Gets the entity that interacted with the composter.
     *
     * @return the entity that composted an item.
     */
    public Entity getEntity() {
        return this.entity;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

}
