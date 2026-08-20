package io.papermc.paper.event.entity;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEventNew;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

/**
 * Called when a LivingEntity loads a crossbow with a projectile.
 */
public interface EntityLoadCrossbowEvent extends EntityEventNew, Cancellable {

    @Override
    LivingEntity getEntity();

    /**
     * Gets the crossbow {@link ItemStack} being loaded.
     *
     * @return the crossbow involved in this event
     */
    ItemStack getCrossbow();

    /**
     * Gets the hand from which the crossbow was loaded.
     *
     * @return the hand
     */
    EquipmentSlot getHand();

    /**
     * @return should the itemstack be consumed
     */
    boolean shouldConsumeItem();

    /**
     * @param consume should the item be consumed
     */
    void setConsumeItem(boolean consume);

    /**
     * Set whether to cancel the crossbow being loaded. If canceled, the
     * projectile that would be loaded into the crossbow will not be consumed.
     */
    @Override
    void setCancelled(boolean cancel);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
