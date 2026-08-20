package org.bukkit.event.player;

import java.util.List;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Unmodifiable;

/**
 * Called when a player shears an entity
 */
public interface PlayerShearEntityEvent extends PlayerEvent, Cancellable {

    /**
     * Gets the entity the player is shearing.
     *
     * @return the entity the player is shearing
     */
    Entity getEntity();

    /**
     * Gets the item used to shear the entity.
     *
     * @return the shears
     */
    ItemStack getItem();

    /**
     * Gets the hand used to shear the entity.
     *
     * @return the hand
     */
    EquipmentSlot getHand();

    /**
     * Gets an immutable list of drops for this shearing.
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
