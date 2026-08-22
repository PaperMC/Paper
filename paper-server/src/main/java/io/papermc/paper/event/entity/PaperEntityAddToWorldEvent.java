package io.papermc.paper.event.entity;

import com.destroystokyo.paper.event.entity.EntityAddToWorldEvent;
import org.bukkit.World;
import org.bukkit.craftbukkit.event.entity.CraftEntityEvent;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;

public class PaperEntityAddToWorldEvent extends CraftEntityEvent implements EntityAddToWorldEvent {

    private final World world;

    public PaperEntityAddToWorldEvent(final Entity entity, final World world) {
        super(entity);
        this.world = world;
    }

    @Override
    public World getWorld() {
        return this.world;
    }

    @Override
    public HandlerList getHandlers() {
        return EntityAddToWorldEvent.getHandlerList();
    }
}
