package org.bukkit.event.server;

import org.bukkit.event.Event;
import org.bukkit.map.MapView;

/**
 * Called when a map is initialized.
 */
public class MapInitializeEvent extends ServerEvent {
    private final MapView mapView;
    
    public MapInitializeEvent(MapView mapView) {
        super(Event.Type.MAP_INITIALIZE);
        this.mapView = mapView;
    }
    
    /**
     * Gets the map initialized in this event.
     * 
     * @return Map for this event
     */
    public MapView getMap() {
        return mapView;
    }
}
