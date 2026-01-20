package org.bukkit.event;

public class TestEvent extends EventTmp {

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
