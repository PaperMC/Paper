package io.papermc.paper.event.entity;

import org.bukkit.craftbukkit.event.entity.CraftEntityEvent;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;

public class PaperEntityToggleSitEvent extends CraftEntityEvent implements EntityToggleSitEvent {

    private final boolean sitting;
    private boolean cancelled;

    public PaperEntityToggleSitEvent(final Entity entity, final boolean sitting) {
        super(entity);
        this.sitting = sitting;
    }

    public PaperEntityToggleSitEvent(final net.minecraft.world.entity.Entity entity, final boolean sitting) {
        this(entity.getBukkitEntity(), sitting);
    }

    @Override
    public boolean getSittingState() {
        return this.sitting;
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
        return EntityToggleSitEvent.getHandlerList();
    }
}
