package org.bukkit.craftbukkit.event.world;

import net.minecraft.Optionull;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.Level;
import org.bukkit.Bukkit;
import org.bukkit.GameEvent;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftGameEvent;
import org.bukkit.craftbukkit.util.CraftLocation;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.world.GenericGameEvent;
import org.checkerframework.checker.index.qual.NonNegative;
import org.jspecify.annotations.Nullable;

import static io.papermc.paper.util.BoundChecker.requireNonNegative;

public class CraftGenericGameEvent extends CraftWorldEvent implements GenericGameEvent {

    private final GameEvent event;
    private final Location location;
    private final @Nullable Entity entity;
    private int radius;

    private boolean cancelled;

    public CraftGenericGameEvent(final GameEvent event, final Location location, final @Nullable Entity entity, final int radius) {
        super(location.getWorld(), !Bukkit.isPrimaryThread());
        this.event = event;
        this.location = location;
        this.entity = entity;
        this.radius = radius;
    }

    public CraftGenericGameEvent(
        final Holder<net.minecraft.world.level.gameevent.GameEvent> gameEvent,
        final Level level,
        final BlockPos pos,
        final net.minecraft.world.entity.@Nullable Entity entity,
        final int radius
    ) {
        this(
            CraftGameEvent.minecraftHolderToBukkit(gameEvent),
            CraftLocation.toBukkit(pos, level),
            Optionull.map(entity, net.minecraft.world.entity.Entity::getBukkitEntity),
            radius
        );
    }

    @Override
    public GameEvent getEvent() {
        return this.event;
    }

    @Override
    public Location getLocation() {
        return this.location.clone();
    }

    @Override
    public @Nullable Entity getEntity() {
        return this.entity;
    }

    @Override
    public int getRadius() {
        return this.radius;
    }

    @Override
    public void setRadius(final @NonNegative int radius) {
        this.radius = requireNonNegative(radius, "radius");
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
        return GenericGameEvent.getHandlerList();
    }
}
