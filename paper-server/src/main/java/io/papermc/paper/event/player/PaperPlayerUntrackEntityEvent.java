package io.papermc.paper.event.player;

import org.bukkit.craftbukkit.event.player.CraftPlayerEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class PaperPlayerUntrackEntityEvent extends CraftPlayerEvent implements PlayerUntrackEntityEvent {

    private final Entity entity;

    public PaperPlayerUntrackEntityEvent(final Player player, final Entity entity) {
        super(player);
        this.entity = entity;
    }

    @Override
    public Entity getEntity() {
        return this.entity;
    }

    @Override
    public HandlerList getHandlers() {
        return PlayerUntrackEntityEvent.getHandlerList();
    }
}
