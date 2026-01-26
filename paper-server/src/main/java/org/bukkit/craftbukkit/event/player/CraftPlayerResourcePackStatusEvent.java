package org.bukkit.craftbukkit.event.player;

import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;

public class CraftPlayerResourcePackStatusEvent extends CraftPlayerEvent implements PlayerResourcePackStatusEvent {

    private final UUID id;
    private final Status status;

    public CraftPlayerResourcePackStatusEvent(final Player player, final UUID id, final Status status) {
        super(player);
        this.id = id;
        this.status = status;
    }

    @Override
    public UUID getID() {
        return this.id;
    }

    @Override
    public Status getStatus() {
        return this.status;
    }

    @Override
    public HandlerList getHandlers() {
        return PlayerResourcePackStatusEvent.getHandlerList();
    }
}
