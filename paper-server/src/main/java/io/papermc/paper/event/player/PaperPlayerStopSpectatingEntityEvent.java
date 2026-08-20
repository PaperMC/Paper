package io.papermc.paper.event.player;

import com.destroystokyo.paper.event.player.PlayerStopSpectatingEntityEvent;
import org.bukkit.craftbukkit.event.player.CraftPlayerEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class PaperPlayerStopSpectatingEntityEvent extends CraftPlayerEvent implements PlayerStopSpectatingEntityEvent {

    private final Entity spectatorTarget;
    private boolean cancelled;

    public PaperPlayerStopSpectatingEntityEvent(final Player player, final Entity spectatorTarget) {
        super(player);
        this.spectatorTarget = spectatorTarget;
    }

    @Override
    public Entity getSpectatorTarget() {
        return this.spectatorTarget;
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
        return PlayerStopSpectatingEntityEvent.getHandlerList();
    }
}
