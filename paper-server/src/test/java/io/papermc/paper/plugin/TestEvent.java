package io.papermc.paper.plugin;

import org.bukkit.craftbukkit.event.CraftEvent;
import org.bukkit.event.HandlerList;

public class TestEvent extends CraftEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    public TestEvent(final boolean async) {
        super(async);
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
