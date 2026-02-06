package io.papermc.paper.event.player;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import com.google.common.base.Preconditions;
import org.bukkit.Location;
import org.bukkit.craftbukkit.event.player.CraftPlayerEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class PaperPlayerJumpEvent extends CraftPlayerEvent implements PlayerJumpEvent {

    private final Location to;
    private Location from;

    private boolean cancelled;

    public PaperPlayerJumpEvent(final Player player, final Location from, final Location to) {
        super(player);
        this.from = from;
        this.to = to;
    }

    @Override
    public Location getFrom() {
        return this.from;
    }

    @Override
    public void setFrom(final Location from) {
        Preconditions.checkArgument(from != null, "Cannot use null from location!");
        Preconditions.checkArgument(from.getWorld() != null, "Cannot use from location with null world!");
        this.from = from.clone();
    }

    @Override
    public Location getTo() {
        return this.to.clone();
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
        return PlayerJumpEvent.getHandlerList();
    }
}
