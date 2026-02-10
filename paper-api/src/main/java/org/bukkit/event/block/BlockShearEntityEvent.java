package org.bukkit.event.block;

import java.util.List;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Unmodifiable;

/**
 * Event fired when a dispenser shears a nearby entity.
 */
public interface BlockShearEntityEvent extends BlockEvent, Cancellable {

    /**
     * Gets the entity that was sheared.
     *
     * @return the entity that was sheared.
     */
    Entity getEntity();

    /**
     * Gets the item used to shear this entity.
     *
     * @return the item used to shear this entity.
     */
    ItemStack getTool();

    /**
     * Get an immutable list of drops for this shearing.
     *
     * @return the shearing drops
     * @see #setDrops(List)
     */
    @Unmodifiable List<ItemStack> getDrops();

    /**
     * Sets the drops for the shearing.
     *
     * @param drops the shear drops
     */
    void setDrops(List<ItemStack> drops);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
