package io.papermc.paper.event.world.border;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.event.HandlerList;

public class PaperWorldBorderCenterChangeEvent extends PaperWorldBorderEvent implements WorldBorderCenterChangeEvent {

    private final Location oldCenter;
    private Location newCenter;

    private boolean cancelled;

    public PaperWorldBorderCenterChangeEvent(final World world, final WorldBorder worldBorder, final Location oldCenter, final Location newCenter) {
        super(world, worldBorder);
        this.oldCenter = oldCenter;
        this.newCenter = newCenter;
    }

    @Override
    public Location getOldCenter() {
        return this.oldCenter.clone();
    }

    @Override
    public Location getNewCenter() {
        return this.newCenter;
    }

    @Override
    public void setNewCenter(final Location newCenter) {
        this.newCenter = newCenter.clone();
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
        return WorldBorderCenterChangeEvent.getHandlerList();
    }
}
