package io.papermc.paper.event.player;

import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.craftbukkit.event.player.CraftPlayerEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class PaperPlayerOpenSignEvent extends CraftPlayerEvent implements PlayerOpenSignEvent {

    private final Sign sign;
    private final Side side;
    private final Cause cause;

    private boolean cancelled;

    public PaperPlayerOpenSignEvent(final Player editor, final Sign sign, final Side side, final Cause cause) {
        super(editor);
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
        return PlayerOpenSignEvent.getHandlerList();
    }
}
