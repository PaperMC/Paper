package io.papermc.paper.event.entity;

import com.destroystokyo.paper.event.entity.TurtleLayEggEvent;
import net.minecraft.core.BlockPos;
import org.bukkit.Location;
import org.bukkit.craftbukkit.event.entity.CraftEntityEvent;
import org.bukkit.craftbukkit.util.CraftLocation;
import org.bukkit.entity.Turtle;
import org.bukkit.event.HandlerList;
import org.checkerframework.common.value.qual.IntRange;

public class PaperTurtleLayEggEvent extends CraftEntityEvent implements TurtleLayEggEvent {

    private final Location location;
    private int eggCount;

    private boolean cancelled;

    public PaperTurtleLayEggEvent(final Turtle turtle, final Location location, final int eggCount) {
        super(turtle);
        this.location = location;
        this.eggCount = eggCount;
    }

    public PaperTurtleLayEggEvent(final net.minecraft.world.entity.animal.turtle.Turtle turtle, final BlockPos pos, final int eggCount) {
        this(
            (Turtle) turtle.getBukkitEntity(),
            CraftLocation.toBukkit(pos, turtle.level()),
            eggCount
        );
    }

    @Override
    public Turtle getEntity() {
        return (Turtle) this.entity;
    }

    @Override
    public Location getLocation() {
        return this.location.clone();
    }

    @Override
    public @IntRange(from = 1, to = 4) int getEggCount() {
        return this.eggCount;
    }

    @Override
    public void setEggCount(final @IntRange(from = 0, to = 4) int eggCount) {
        if (eggCount < 1) {
            this.cancelled = true;
            return;
        }
        this.eggCount = Math.min(eggCount, 4);
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
        return TurtleLayEggEvent.getHandlerList();
    }
}
