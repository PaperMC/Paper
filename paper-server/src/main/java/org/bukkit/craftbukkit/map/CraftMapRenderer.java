package org.bukkit.craftbukkit.map;

import net.minecraft.server.WorldMap;
import net.minecraft.server.WorldMapOrienter;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.map.MapCursorCollection;

public class CraftMapRenderer extends MapRenderer {
    
    private final CraftMapView mapView;
    private final WorldMap worldMap;

    public CraftMapRenderer(CraftMapView mapView, WorldMap worldMap) {
        super(false);
        this.mapView = mapView;
        this.worldMap = worldMap;
    }

    @Override
    public void render(MapView map, MapCanvas canvas, Player player) {
        // Map
        for (int x = 0; x < 128; ++x) {
            for (int y = 0; y < 128; ++y) {
                canvas.setPixel(x, y, worldMap.f[y * 128 + x]);
            }
        }
        
        // Cursors
        MapCursorCollection cursors = canvas.getCursors();
        while (cursors.size() > 0) {
            cursors.removeCursor(cursors.getCursor(0));
        }
        for (int i = 0; i < worldMap.i.size(); ++i) {
            WorldMapOrienter orienter = (WorldMapOrienter) worldMap.i.get(i);
            cursors.addCursor(orienter.b, orienter.c, (byte)(orienter.d & 15), (byte)(orienter.a));
        }
    }
    
}
