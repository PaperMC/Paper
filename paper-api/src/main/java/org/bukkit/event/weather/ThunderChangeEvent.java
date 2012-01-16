package org.bukkit.event.weather;

import org.bukkit.World;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Stores data for thunder state changing in a world
 */
@SuppressWarnings("serial")
public class ThunderChangeEvent extends WeatherEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private boolean canceled;
    private boolean to;

    public ThunderChangeEvent(World world, boolean to) {
        super(Type.THUNDER_CHANGE, world);
        this.to = to;
    }

    public boolean isCancelled() {
        return canceled;
    }

    public void setCancelled(boolean cancel) {
        canceled = cancel;
    }

    /**
     * Gets the state of thunder that the world is being set to
     *
     * @return true if the weather is being set to thundering, false otherwise
     */
    public boolean toThunderState() {
        return to;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
