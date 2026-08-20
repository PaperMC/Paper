package org.bukkit.craftbukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerChannelEvent;

public abstract class CraftPlayerChannelEvent extends CraftPlayerEvent implements PlayerChannelEvent {

    private final String channel;

    protected CraftPlayerChannelEvent(final Player player, final String channel) {
        super(player);
        this.channel = channel;
    }

    @Override
    public final String getChannel() {
        return this.channel;
    }

    @Override
    public HandlerList getHandlers() {
        return PlayerChannelEvent.getHandlerList();
    }
}
