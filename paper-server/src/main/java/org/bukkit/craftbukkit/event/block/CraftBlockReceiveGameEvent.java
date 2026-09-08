package org.bukkit.craftbukkit.event.block;

import net.minecraft.Optionull;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.bukkit.GameEvent;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftGameEvent;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockReceiveGameEvent;
import org.jspecify.annotations.Nullable;

public class CraftBlockReceiveGameEvent extends CraftBlockEvent implements BlockReceiveGameEvent {

    private final GameEvent event;
    private final @Nullable Entity entity;

    private boolean cancelled;

    public CraftBlockReceiveGameEvent(final GameEvent event, final Block block, final @Nullable Entity entity) {
        super(block);
        this.event = event;
        this.entity = entity;
    }

    public CraftBlockReceiveGameEvent(
        final Holder<net.minecraft.world.level.gameevent.GameEvent> event,
        final Level level,
        final Vec3 pos,
        final net.minecraft.world.entity.@Nullable Entity entity
    ) {
        this(
            CraftGameEvent.minecraftHolderToBukkit(event),
            CraftBlock.at(level, BlockPos.containing(pos)),
            Optionull.map(entity, net.minecraft.world.entity.Entity::getBukkitEntity)
        );
    }

    @Override
    public GameEvent getEvent() {
        return this.event;
    }

    @Override
    public @Nullable Entity getEntity() {
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

    @Override
    public HandlerList getHandlers() {
        return BlockReceiveGameEvent.getHandlerList();
    }
}
