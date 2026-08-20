package org.bukkit.craftbukkit.event.player;

import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerSignOpenEvent;

public class CraftPlayerSignOpenEvent extends CraftPlayerEvent implements PlayerSignOpenEvent {

    private final Sign sign;
    private final Side side;
    private final Cause cause;

    private boolean cancelled;

    public CraftPlayerSignOpenEvent(final Player player, final Sign sign, final Side side, final Cause cause) {
        super(player);
        this.sign = sign;
        this.side = side;
        this.cause = cause;
    }

    @Override
    public Sign getSign() {
        return this.sign;
    }

    @Override
    public Side getSide() {
        return this.side;
    }

    @Override
    public Cause getCause() {
        return this.cause;
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
        return PlayerSignOpenEvent.getHandlerList();
    }
}
