package io.papermc.paper.event.world.border;

import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.craftbukkit.event.world.CraftWorldEvent;

public abstract class PaperWorldBorderEvent extends CraftWorldEvent implements WorldBorderEvent {

    protected final WorldBorder worldBorder;

    protected PaperWorldBorderEvent(final World world, final WorldBorder worldBorder) {
        super(world);
        this.worldBorder = worldBorder;
    }

    @Override
    public WorldBorder getWorldBorder() {
        return this.worldBorder;
    }
}
