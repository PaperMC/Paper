package com.destroystokyo.paper.event.entity;

import org.bukkit.entity.Witch;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.Nullable;

/**
 * Fired when a witch consumes the potion in their hand to buff themselves.
 */
public interface WitchConsumePotionEvent extends EntityEvent, Cancellable {

    @Override
    Witch getEntity();

    /**
     * @return the potion the witch will consume and have the effects applied.
     */
    @Nullable ItemStack getPotion();

    /**
     * Sets the potion to be consumed and applied to the witch.
     *
     * @param potion The potion
     */
    void setPotion(@Nullable ItemStack potion);

    /**
     * @return Event was cancelled or potion was {@code null}
     */
    @Override
    boolean isCancelled();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
