package org.bukkit.craftbukkit.event.player;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerHideEntityEvent;

public class CraftPlayerHideEntityEvent extends CraftPlayerEvent implements PlayerHideEntityEvent {

    private final Entity entity;

    public CraftPlayerHideEntityEvent(final Player player, final Entity entity) {
        super(player);
        this.entity = entity;
    }

    @Override
    public Entity getEntity() {
        return this.entity;
    }

    @Override
    public HandlerList getHandlers() {
        return PlayerHideEntityEvent.getHandlerList();
    }
}
