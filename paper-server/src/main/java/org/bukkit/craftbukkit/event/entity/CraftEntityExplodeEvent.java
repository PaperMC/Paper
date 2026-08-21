package org.bukkit.craftbukkit.event.entity;

import java.util.List;
import org.bukkit.ExplosionResult;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityExplodeEvent;

public class CraftEntityExplodeEvent extends CraftEntityEvent implements EntityExplodeEvent {

    private final Location location;
    private final List<Block> blocks;
    private float yield;
    private final ExplosionResult result;

    private boolean cancelled;

    public CraftEntityExplodeEvent(final Entity entity, final Location location, final List<Block> blocks, final float yield, final ExplosionResult result) {
        super(entity);
        this.location = location;
        this.blocks = blocks;
        this.yield = yield;
        this.result = result;
    }

    @Override
    public ExplosionResult getExplosionResult() {
        return this.result;
    }

    @Override
    public List<Block> blockList() {
        return this.blocks;
    }

    @Override
    public Location getLocation() {
        return this.location.clone();
    }

    @Override
    public float getYield() {
        return this.yield;
    }

    @Override
    public void setYield(final float yield) {
        this.yield = yield;
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
        return EntityExplodeEvent.getHandlerList();
    }
}
