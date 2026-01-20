package io.papermc.paper.event.player;

import org.bukkit.craftbukkit.event.player.CraftPlayerEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class PaperPlayerDeepSleepEvent extends CraftPlayerEvent implements PlayerDeepSleepEvent {

    private boolean cancelled;

    public PaperPlayerDeepSleepEvent(final Player player) {
        super(player);
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
        return PlayerDeepSleepEvent.getHandlerList();
    }
}
