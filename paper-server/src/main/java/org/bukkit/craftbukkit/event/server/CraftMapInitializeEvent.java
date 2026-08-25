package org.bukkit.craftbukkit.event.server;

import org.bukkit.event.HandlerList;
import org.bukkit.event.server.MapInitializeEvent;
import org.bukkit.map.MapView;

public class CraftMapInitializeEvent extends CraftServerEvent implements MapInitializeEvent {

    private final MapView mapView;

    public CraftMapInitializeEvent(final MapView mapView) {
        this.mapView = mapView;
    }

    @Override
    public MapView getMap() {
        return this.mapView;
    }

    @Override
    public HandlerList getHandlers() {
        return MapInitializeEvent.getHandlerList();
    }
}
