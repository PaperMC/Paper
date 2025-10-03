package io.papermc.paper.plugin;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class TestEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    public TestEvent(boolean async) {
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
