package org.bukkit.craftbukkit.event.world;

import org.bukkit.World;
import org.bukkit.event.HandlerList;
import org.bukkit.event.world.TimeSkipEvent;

public class CraftTimeSkipEvent extends CraftClockTimeSkipEvent implements TimeSkipEvent {

    private final World world;

    public CraftTimeSkipEvent(final World world, final SkipReason skipReason, final long skipAmount) {
        super(skipReason, skipAmount);
        this.world = world;
    }

    @Override
    public World getWorld() {
        return this.world;
    }

    @Override
    public HandlerList getHandlers() {
        return TimeSkipEvent.getHandlerList();
    }
}
