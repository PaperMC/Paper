package io.papermc.paper.event.entity;

import org.bukkit.block.Block;
import org.bukkit.craftbukkit.event.entity.CraftEntityEvent;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;

public class PaperItemTransportingEntityValidateTargetEvent extends CraftEntityEvent implements ItemTransportingEntityValidateTargetEvent {

    private final Block block;
    private boolean allowed = true;

    public PaperItemTransportingEntityValidateTargetEvent(final Entity entity, final Block block) {
        super(entity);
        this.block = block;
    }

    @Override
    public Block getBlock() {
        return this.block;
    }

    @Override
    public boolean isAllowed() {
        return this.allowed;
    }

    @Override
    public void setAllowed(final boolean allowed) {
        this.allowed = allowed;
    }

    @Override
    public HandlerList getHandlers() {
        return ItemTransportingEntityValidateTargetEvent.getHandlerList();
    }
}
