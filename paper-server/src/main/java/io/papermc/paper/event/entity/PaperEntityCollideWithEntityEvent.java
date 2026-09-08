package io.papermc.paper.event.entity;

import java.util.List;
import org.bukkit.craftbukkit.event.CraftEvent;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;

public class PaperEntityCollideWithEntityEvent extends CraftEvent implements EntityCollideWithEntityEvent {

    private boolean cancelled;
    private final List<Entity> entities;

    public PaperEntityCollideWithEntityEvent(final Entity entity1, final Entity entity2) {
        this.entities = List.of(entity1, entity2);
    }

    public PaperEntityCollideWithEntityEvent(final net.minecraft.world.entity.Entity entity1, final net.minecraft.world.entity.Entity entity2) {
        this(entity1.getBukkitEntity(), entity2.getBukkitEntity());
    }

    @Override
    public List<Entity> getEntities() {
        return this.entities;
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
        return EntityCollideWithEntityEvent.getHandlerList();
    }
}
