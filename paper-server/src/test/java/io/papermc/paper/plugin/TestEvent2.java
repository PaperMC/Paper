package io.papermc.paper.plugin;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class TestEvent2 extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    public TestEvent2(boolean async) {
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
