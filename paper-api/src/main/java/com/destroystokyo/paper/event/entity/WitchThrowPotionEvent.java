package com.destroystokyo.paper.event.entity;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Witch;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEventNew;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.Nullable;

/**
 * Fired when a witch throws a potion at a player
 */
public interface WitchThrowPotionEvent extends EntityEventNew, Cancellable {

    @Override
    Witch getEntity();

    /**
     * @return The target of the potion
     */
    LivingEntity getTarget();

    /**
     * @return The potion the witch will throw at a player
     */
    @Nullable ItemStack getPotion();

    /**
     * Sets the potion to be thrown at a player
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
