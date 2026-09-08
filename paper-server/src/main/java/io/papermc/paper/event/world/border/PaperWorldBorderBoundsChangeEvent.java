package io.papermc.paper.event.world.border;

import com.google.common.primitives.Ints;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.event.HandlerList;
import org.checkerframework.checker.index.qual.NonNegative;

import static io.papermc.paper.util.BoundChecker.requireNonNegative;

public class PaperWorldBorderBoundsChangeEvent extends PaperWorldBorderEvent implements WorldBorderBoundsChangeEvent {

    private Type type;
    private final double oldSize;
    private double newSize;
    private long duration;
    private boolean cancelled;

    public PaperWorldBorderBoundsChangeEvent(final World world, final WorldBorder worldBorder, final Type type, final double oldSize, final double newSize, final long duration) {
        super(world, worldBorder);
        this.type = type;
        this.oldSize = oldSize;
        this.newSize = newSize;
        this.duration = duration;
    }

    @Override
    public Type getType() {
        return this.type;
    }

    @Override
    public double getOldSize() {
        return this.oldSize;
    }

    @Override
    public double getNewSize() {
        return this.newSize;
    }

    @Override
    public void setNewSize(final double newSize) {
        this.newSize = Math.clamp(newSize, 1.0, this.worldBorder.getMaxSize());
    }

    @Override
    public @NonNegative long getDurationTicks() {
        return this.duration;
    }

    @Override
    public void setDurationTicks(final @NonNegative long duration) {
        this.duration = Ints.saturatedCast(requireNonNegative(duration, "duration"));
        if (this.type == Type.INSTANT_MOVE) {
            this.type = Type.STARTED_MOVE;
        }
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
        return WorldBorderBoundsChangeEvent.getHandlerList();
    }
}
