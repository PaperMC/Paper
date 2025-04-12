package org.bukkit.craftbukkit.map;

import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapCursor;
import org.bukkit.map.MapCursorCollection;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

public class CraftMapRenderer extends MapRenderer {

    private final MapItemSavedData worldMap;

    public CraftMapRenderer(MapItemSavedData worldMap) {
        super(false);
        this.worldMap = worldMap;
    }

    @Override
    public void render(MapView map, MapCanvas canvas, Player player) {
        // Map
        for (int y = 0; y < 128; ++y) {
            for (int x = 0; x < 128; ++x) {
                canvas.setPixel(x, y, this.worldMap.colors[y * 128 + x]);
            }
        }

        // Cursors
        MapCursorCollection cursors = canvas.getCursors();
        while (cursors.size() > 0) {
            cursors.removeCursor(cursors.getCursor(0));
        }

        for (String key : this.worldMap.decorations.keySet()) {
            // If this cursor is for a player check visibility with vanish system
            Player other = Bukkit.getPlayerExact(key);
            if (other != null && !player.canSee(other)) {
                continue;
            }

            MapDecoration decoration = this.worldMap.decorations.get(key);
            cursors.addCursor(new MapCursor(decoration.x(), decoration.y(), (byte) (decoration.rot() & 15), CraftMapCursor.CraftType.minecraftHolderToBukkit(decoration.type()), true, decoration.name().isEmpty() ? null : io.papermc.paper.adventure.PaperAdventure.asAdventure(decoration.name().get()))); // Paper
        }
    }

}
