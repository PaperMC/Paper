package com.destroystokyo.paper.event.entity;

import io.papermc.paper.event.entity.EntityIgniteEvent;
import org.bukkit.entity.Creeper;

/**
 * Called when a Creeper is ignited either by a
 * flint and steel, {@link Creeper#ignite()} or
 * {@link Creeper#setIgnited(boolean)}.
 */
public interface CreeperIgniteEvent extends EntityIgniteEvent {

    @Override
    Creeper getEntity();

    boolean isIgnited();

    void setIgnited(boolean ignited);
}
