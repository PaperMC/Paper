package io.papermc.paper.event.world.border;

import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.event.world.WorldEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
public abstract class WorldBorderEvent extends WorldEvent {

    protected final WorldBorder worldBorder;

    @ApiStatus.Internal
    protected WorldBorderEvent(final World world, final WorldBorder worldBorder) {
        super(world);
        this.worldBorder = worldBorder;
    }

    public WorldBorder getWorldBorder() {
        return this.worldBorder;
    }
}
