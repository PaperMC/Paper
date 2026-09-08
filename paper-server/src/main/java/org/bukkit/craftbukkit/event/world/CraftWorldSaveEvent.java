package org.bukkit.craftbukkit.event.world;

import net.minecraft.world.level.Level;
import org.bukkit.event.HandlerList;
import org.bukkit.event.world.WorldSaveEvent;

public class CraftWorldSaveEvent extends CraftWorldEvent implements WorldSaveEvent {

    public CraftWorldSaveEvent(final Level level) {
        super(level);
    }

    @Override
    public HandlerList getHandlers() {
        return WorldSaveEvent.getHandlerList();
    }
}
