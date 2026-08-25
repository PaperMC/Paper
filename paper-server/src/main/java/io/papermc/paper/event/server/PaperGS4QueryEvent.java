package io.papermc.paper.event.server;

import com.destroystokyo.paper.event.server.GS4QueryEvent;
import com.google.common.base.Preconditions;
import java.net.InetAddress;
import org.bukkit.craftbukkit.event.CraftEvent;
import org.bukkit.event.HandlerList;

public class PaperGS4QueryEvent extends CraftEvent implements GS4QueryEvent {

    private final QueryType queryType;
    private final InetAddress querierAddress;
    private QueryResponse response;

    public PaperGS4QueryEvent(final QueryType queryType, final InetAddress querierAddress, final QueryResponse response) {
        super(true); // should always be called async
        this.queryType = queryType;
        this.querierAddress = querierAddress;
        this.response = response;
    }

    @Override
    public QueryType getQueryType() {
        return this.queryType;
    }

    @Override
    public InetAddress getQuerierAddress() {
        return this.querierAddress;
    }

    @Override
    public QueryResponse getResponse() {
        return this.response;
    }

    @Override
    public void setResponse(final QueryResponse response) {
        Preconditions.checkArgument(response != null, "response cannot be null");
        this.response = response;
    }

    @Override
    public HandlerList getHandlers() {
        return GS4QueryEvent.getHandlerList();
    }
}
