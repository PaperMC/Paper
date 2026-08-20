package org.bukkit.craftbukkit.event.player;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class CraftPlayerChangedWorldEvent extends CraftPlayerEvent implements PlayerChangedWorldEvent {

    private final World from;

    public CraftPlayerChangedWorldEvent(final Player player, final World from) {
        super(player);
        this.from = from;
    }

    @Override
    public World getFrom() {
        return this.from;
    }

    @Override
    public HandlerList getHandlers() {
        return PlayerChangedWorldEvent.getHandlerList();
    }
}
