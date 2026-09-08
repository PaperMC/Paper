package org.bukkit.craftbukkit.event.world;

import net.minecraft.world.level.Level;
import org.bukkit.World;
import org.bukkit.event.world.TimeSkipEvent;

public class CraftTimeSkipEvent extends CraftClockTimeSkipEvent implements TimeSkipEvent {

    private final World world;

    public CraftTimeSkipEvent(final World world, final SkipReason skipReason, final long skipAmount) {
        super(skipReason, skipAmount);
        this.world = world;
    }

    public CraftTimeSkipEvent(final Level level, SkipReason skipReason, final long skipAmount) {
        this(level.getWorld(), skipReason, skipAmount);
    }

    @Override
    public World getWorld() {
        return this.world;
    }
}
