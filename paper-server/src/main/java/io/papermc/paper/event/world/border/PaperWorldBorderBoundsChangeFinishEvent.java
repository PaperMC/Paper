package io.papermc.paper.event.world.border;

import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.event.HandlerList;

public class PaperWorldBorderBoundsChangeFinishEvent extends PaperWorldBorderEvent implements WorldBorderBoundsChangeFinishEvent {

    private final double oldSize;
    private final double newSize;
    private final double duration;

    public PaperWorldBorderBoundsChangeFinishEvent(final World world, final WorldBorder worldBorder, final double oldSize, final double newSize, final double duration) {
        super(world, worldBorder);
        this.oldSize = oldSize;
        this.newSize = newSize;
        this.duration = duration;
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
    public double getDuration() {
        return this.duration;
    }

    @Override
    public HandlerList getHandlers() {
        return WorldBorderBoundsChangeFinishEvent.getHandlerList();
    }
}
