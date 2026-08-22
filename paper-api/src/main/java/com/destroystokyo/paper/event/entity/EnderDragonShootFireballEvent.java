package com.destroystokyo.paper.event.entity;

import org.bukkit.entity.DragonFireball;
import org.bukkit.entity.EnderDragon;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEventNew;

/**
 * Fired when an EnderDragon shoots a fireball
 */
public interface EnderDragonShootFireballEvent extends EntityEventNew, Cancellable {

    /**
     * The enderdragon shooting the fireball
     */
    @Override
    EnderDragon getEntity();

    /**
     * @return The fireball being shot
     */
    DragonFireball getFireball();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
