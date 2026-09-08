package io.papermc.paper.plugin;

import org.bukkit.craftbukkit.event.CraftEvent;
import org.bukkit.event.HandlerList;

public class CraftTestEvent extends CraftEvent implements TestEvent {

    public CraftTestEvent(final boolean async) {
        super(async);
    }

    @Override
    public HandlerList getHandlers() {
        return TestEvent.getHandlerList();
    }
}
