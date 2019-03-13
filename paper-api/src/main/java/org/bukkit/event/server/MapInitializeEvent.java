package org.bukkit.event.server;

import org.bukkit.event.HandlerList;
import org.bukkit.map.MapView;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a map is initialized.
 */
public class MapInitializeEvent extends ServerEvent {
    private static final HandlerList handlers = new HandlerList();
    private final MapView mapView;

    public MapInitializeEvent(@NotNull final MapView mapView) {
        this.mapView = mapView;
    }

    /**
     * Gets the map initialized in this event.
     *
     * @return Map for this event
     */
    @NotNull
    public MapView getMap() {
        return mapView;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
