package io.papermc.paper.event.entity;

import com.google.common.base.Preconditions;
import org.bukkit.craftbukkit.event.entity.CraftEntityEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Warden;
import org.bukkit.event.HandlerList;
import org.checkerframework.common.value.qual.IntRange;

public class PaperWardenAngerChangeEvent extends CraftEntityEvent implements WardenAngerChangeEvent {

    private final Entity target;
    private final int oldAnger;
    private int newAnger;

    private boolean cancelled;

    public PaperWardenAngerChangeEvent(final Warden warden, final Entity target, final int oldAnger, final int newAnger) {
        super(warden);
        this.target = target;
        this.oldAnger = oldAnger;
        this.newAnger = newAnger;
    }

    @Override
    public Entity getTarget() {
        return this.target;
    }

    @Override
    public @IntRange(from = 0, to = 150) int getOldAnger() {
        return this.oldAnger;
    }

    @Override
    public @IntRange(from = 0, to = 150) int getNewAnger() {
        return this.newAnger;
    }

    @Override
    public void setNewAnger(final @IntRange(from = 0, to = 150) int newAnger) {
        Preconditions.checkArgument(newAnger <= 150, "newAnger must not be greater than 150");
        this.newAnger = newAnger;
    }

    @Override
    public Warden getEntity() {
        return (Warden) this.entity;
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
        return WardenAngerChangeEvent.getHandlerList();
    }
}
