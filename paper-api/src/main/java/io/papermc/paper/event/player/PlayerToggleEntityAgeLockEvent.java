package io.papermc.paper.event.player;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

/**
 * Called when a player toggles the age lock of an entity using an item.
 */
public interface PlayerToggleEntityAgeLockEvent extends PlayerEvent, Cancellable {

    /**
     * {@return the entity that is having its age locked or unlocked}
     */
    LivingEntity getEntity();

    /**
     * {@return the item being used to toggle the age lock of the entity}
     */
    ItemStack getItem();

    /**
     * {@return the hand being used to toggle the age lock of the entity}
     */
    EquipmentSlot getHand();

    /**
     * {@return whether the age of the entity is going to be locked or not}
     */
    boolean isAgeLocked();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
