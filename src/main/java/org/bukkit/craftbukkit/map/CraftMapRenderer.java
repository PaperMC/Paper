package org.bukkit.craftbukkit.map;

import net.minecraft.server.WorldMap;
import net.minecraft.server.WorldMapDecoration;

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
                canvas.setPixel(x, y, worldMap.colors[y * 128 + x]);
            }
        }

        // Cursors
        MapCursorCollection cursors = canvas.getCursors();
        while (cursors.size() > 0) {
            cursors.removeCursor(cursors.getCursor(0));
        }
        for (int i = 0; i < worldMap.decorations.size(); ++i) {
            WorldMapDecoration decoration = (WorldMapDecoration) worldMap.decorations.get(i);
            cursors.addCursor(decoration.locX, decoration.locY, (byte) (decoration.rotation & 15), (byte) (decoration.type));
        }
    }

}
