package org.bukkit.craftbukkit.event.world;

import net.minecraft.world.level.Level;
import org.bukkit.event.HandlerList;
import org.bukkit.event.world.WorldInitEvent;

public class CraftWorldInitEvent extends CraftWorldEvent implements WorldInitEvent {

    public CraftWorldInitEvent(final Level level) {
        super(level);
    }

    @Override
    public HandlerList getHandlers() {
        return WorldInitEvent.getHandlerList();
    }
}
