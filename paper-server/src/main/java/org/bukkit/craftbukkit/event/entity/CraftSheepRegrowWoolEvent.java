package org.bukkit.craftbukkit.event.entity;

import org.bukkit.entity.Sheep;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.SheepRegrowWoolEvent;

public class CraftSheepRegrowWoolEvent extends CraftEntityEvent implements SheepRegrowWoolEvent {

    private boolean cancelled;

    public CraftSheepRegrowWoolEvent(final Sheep sheep) {
        super(sheep);
    }

    @Override
    public Sheep getEntity() {
        return (Sheep) this.entity;
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
        return SheepRegrowWoolEvent.getHandlerList();
    }
}
