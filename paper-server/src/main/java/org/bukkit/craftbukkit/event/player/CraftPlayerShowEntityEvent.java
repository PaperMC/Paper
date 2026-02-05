package org.bukkit.craftbukkit.event.player;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerShowEntityEvent;

public class CraftPlayerShowEntityEvent extends CraftPlayerEvent implements PlayerShowEntityEvent {

    private final Entity entity;

    public CraftPlayerShowEntityEvent(final Player player, final Entity entity) {
        super(player);
        this.entity = entity;
    }

    @Override
    public Entity getEntity() {
        return this.entity;
    }

    @Override
    public HandlerList getHandlers() {
        return PlayerShowEntityEvent.getHandlerList();
    }
}
