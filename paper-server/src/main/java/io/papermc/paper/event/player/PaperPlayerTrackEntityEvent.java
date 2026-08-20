package io.papermc.paper.event.player;

import org.bukkit.craftbukkit.event.player.CraftPlayerEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class PaperPlayerTrackEntityEvent extends CraftPlayerEvent implements PlayerTrackEntityEvent {

    private final Entity entity;
    private boolean cancelled;

    public PaperPlayerTrackEntityEvent(final Player player, final Entity entity) {
        super(player);
        this.entity = entity;
    }

    @Override
    public Entity getEntity() {
        return this.entity;
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
        return PlayerTrackEntityEvent.getHandlerList();
    }
}
