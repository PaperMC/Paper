package io.papermc.paper.event.player;

import org.bukkit.Location;
import org.bukkit.craftbukkit.event.player.CraftPlayerEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class PaperPlayerFailMoveEvent extends CraftPlayerEvent implements PlayerFailMoveEvent {

    private final FailReason failReason;
    private final Location from;
    private final Location to;
    private boolean allowed;
    private boolean logWarning;

    public PaperPlayerFailMoveEvent(final Player player, final FailReason failReason, final boolean allowed, final boolean logWarning, final Location from, final Location to) {
        super(player);
        this.failReason = failReason;
        this.allowed = allowed;
        this.logWarning = logWarning;
        this.from = from;
        this.to = to;
    }

    @Override
    public FailReason getFailReason() {
        return this.failReason;
    }

    @Override
    public Location getFrom() {
        return this.from.clone();
    }

    @Override
    public Location getTo() {
        return this.to.clone();
    }

    @Override
    public boolean isAllowed() {
        return this.allowed;
    }

    @Override
    public void setAllowed(final boolean allowed) {
        this.allowed = allowed;
    }

    @Override
    public boolean getLogWarning() {
        return this.logWarning;
    }

    @Override
    public void setLogWarning(final boolean logWarning) {
        this.logWarning = logWarning;
    }

    @Override
    public HandlerList getHandlers() {
        return PlayerFailMoveEvent.getHandlerList();
    }

}
