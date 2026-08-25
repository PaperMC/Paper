package org.bukkit.craftbukkit.event.world;

import java.util.List;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.world.PortalCreateEvent;
import org.jspecify.annotations.Nullable;

public class CraftPortalCreateEvent extends CraftWorldEvent implements PortalCreateEvent {

    private final List<BlockState> blocks;
    private final Entity entity;
    private final CreateReason reason;

    private boolean cancelled;

    public CraftPortalCreateEvent(final List<BlockState> blocks, final World world, final @Nullable Entity entity, final CreateReason reason) {
        super(world);

        this.blocks = blocks;
        this.entity = entity;
        this.reason = reason;
    }

    @Override
    public List<BlockState> getBlocks() {
        return this.blocks;
    }

    @Override
    public @Nullable Entity getEntity() {
        return this.entity;
    }

    @Override
    public CreateReason getReason() {
        return this.reason;
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
        return PortalCreateEvent.getHandlerList();
    }
}
