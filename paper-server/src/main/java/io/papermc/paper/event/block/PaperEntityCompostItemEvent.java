package io.papermc.paper.event.block;

import io.papermc.paper.event.entity.EntityCompostItemEvent;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

public class PaperEntityCompostItemEvent extends PaperCompostItemEvent implements EntityCompostItemEvent {

    private final Entity entity;
    private boolean cancelled;

    public PaperEntityCompostItemEvent(final Entity entity, final Block composter, final ItemStack item, final boolean willRaiseLevel) {
        super(composter, item, willRaiseLevel);
        this.entity = entity;
    }

    @Override
    public Entity getEntity() {
        return this.entity;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }
}
