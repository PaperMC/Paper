package com.destroystokyo.paper.event.entity;

import org.bukkit.entity.Witch;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEventNew;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.Nullable;

public interface WitchReadyPotionEvent extends EntityEventNew, Cancellable {

    @Override
    Witch getEntity();

    /**
     * @return the potion the witch is readying to use
     */
    @Nullable ItemStack getPotion();

    /**
     * Sets the potion the which is going to hold and use
     *
     * @param potion The potion
     */
    void setPotion(@Nullable ItemStack potion);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
