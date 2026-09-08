package org.bukkit.craftbukkit.event.world;

import net.minecraft.world.level.Level;
import org.bukkit.event.HandlerList;
import org.bukkit.event.world.WorldLoadEvent;

public class CraftWorldLoadEvent extends CraftWorldEvent implements WorldLoadEvent {

    public CraftWorldLoadEvent(final Level level) {
        super(level);
    }

    @Override
    public HandlerList getHandlers() {
        return WorldLoadEvent.getHandlerList();
    }
}
