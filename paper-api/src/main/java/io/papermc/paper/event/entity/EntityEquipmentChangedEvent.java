package io.papermc.paper.event.entity;

import java.util.Map;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEventNew;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;

/**
 * Called whenever a change to an entity's equipment has been detected. This event is called after effects from
 * attribute modifiers and enchantments have been updated.
 * <p>
 * Examples of actions that can trigger this event:
 * <ul>
 *     <li>An entity being added to a world.</li>
 *     <li>A player logging in.</li>
 *     <li>The durability of an equipment item changing.</li>
 *     <li>A dispenser equipping an item onto an entity.</li>
 *     <li>An entity picking up an armor or weapon item from the ground.</li>
 *     <li>A player changing their equipped armor.</li>
 *     <li>A player changes their currently held item.</li>
 * </ul>
 */
public interface EntityEquipmentChangedEvent extends EntityEventNew {

    @Override
    LivingEntity getEntity();

    /**
     * Gets a map of changed slots to their respective equipment changes.
     *
     * @return the equipment changes map
     */
    @Unmodifiable Map<EquipmentSlot, EquipmentChange> getEquipmentChanges();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }

    /**
     * Represents a change in equipment for a single equipment slot.
     */
    @ApiStatus.NonExtendable
    interface EquipmentChange {

        /**
         * Gets the existing item that is being replaced.
         *
         * @return the existing item
         */
        @Contract(pure = true, value = "-> new")
        ItemStack oldItem();

        /**
         * Gets the new item that is replacing the existing item.
         *
         * @return the new item
         */
        @Contract(pure = true, value = "-> new")
        ItemStack newItem();
    }
}
