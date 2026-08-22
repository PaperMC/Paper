package org.bukkit.craftbukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.PigZombie;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.PigZombieAngerEvent;
import org.jspecify.annotations.Nullable;

public class CraftPigZombieAngerEvent extends CraftEntityEvent implements PigZombieAngerEvent {

    private final Entity target;
    private int newAnger;

    private boolean cancelled;

    public CraftPigZombieAngerEvent(final PigZombie zombifiedPiglin, final @Nullable Entity target, final int newAnger) {
        super(zombifiedPiglin);
        this.target = target;
        this.newAnger = newAnger;
    }

    @Override
    public @Nullable Entity getTarget() {
        return this.target;
    }

    @Override
    public int getNewAnger() {
        return this.newAnger;
    }

    @Override
    public void setNewAnger(final int newAnger) {
        this.newAnger = newAnger;
    }

    @Override
    public PigZombie getEntity() {
        return (PigZombie) this.entity;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return PigZombieAngerEvent.getHandlerList();
    }
}
