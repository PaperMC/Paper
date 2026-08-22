package org.bukkit.event.entity;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.EquipmentSlot;
import org.jspecify.annotations.Nullable;

/**
 * Called when an entity dies and may have the opportunity to be resurrected.
 * Will be called in a cancelled state if the entity does not have a totem
 * equipped.
 */
public interface EntityResurrectEvent extends EntityEvent, Cancellable {

    /**
     * Get the hand in which the totem of undying was found, or {@code null} if the
     * entity did not have a totem of undying.
     *
     * @return the hand, or {@code null}
     */
    @Nullable EquipmentSlot getHand();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
