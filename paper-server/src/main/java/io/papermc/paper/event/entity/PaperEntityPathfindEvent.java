package io.papermc.paper.event.entity;

import com.destroystokyo.paper.event.entity.EntityPathfindEvent;
import net.minecraft.Optionull;
import net.minecraft.core.BlockPos;
import org.bukkit.Location;
import org.bukkit.craftbukkit.event.entity.CraftEntityEvent;
import org.bukkit.craftbukkit.util.CraftLocation;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.Nullable;

public class PaperEntityPathfindEvent extends CraftEntityEvent implements EntityPathfindEvent {

    private final @Nullable Entity targetEntity;
    private final Location targetLocation;

    private boolean cancelled;

    public PaperEntityPathfindEvent(final Entity entity, final Location targetLocation, final @Nullable Entity targetEntity) {
        super(entity);
        this.targetEntity = targetEntity;
        this.targetLocation = targetLocation;
    }

    public PaperEntityPathfindEvent(
        final net.minecraft.world.entity.Entity entity, final BlockPos target, final net.minecraft.world.entity.@Nullable Entity targetEntity
    ) {
        this(
            entity.getBukkitEntity(),
            CraftLocation.toBukkit(target, entity.level()),
            Optionull.map(targetEntity, net.minecraft.world.entity.Entity::getBukkitEntity)
        );
    }

    @Override
    public @Nullable Entity getTargetEntity() {
        return this.targetEntity;
    }

    @Override
    public Location getTargetLocation() {
        return this.targetLocation.clone();
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
        return EntityPathfindEvent.getHandlerList();
    }
}
