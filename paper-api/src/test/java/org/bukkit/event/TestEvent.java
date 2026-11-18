package org.bukkit.event;


public class TestEvent extends Event {

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
