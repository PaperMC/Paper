package org.bukkit.map;

import static org.junit.jupiter.api.Assertions.*;
import java.awt.Color;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;
import org.bukkit.craftbukkit.map.CraftMapColorCache;
import org.bukkit.support.environment.Slow;
import org.junit.jupiter.api.Test;

public class MapTest {

    private static final Logger logger = Logger.getLogger("MapTest");

    @Test
    @Slow("Test takes around 25 seconds, should be run by changes to the map color conversion")
    public void testMapColorCacheBuilding() throws ExecutionException, InterruptedException {
        CraftMapColorCache craftMapColorCache = new CraftMapColorCache(MapTest.logger);
        craftMapColorCache.initCache().get();

        for (int r = 0; r < 256; r++) {
            for (int g = 0; g < 256; g++) {
                for (int b = 0; b < 256; b++) {
                    Color color = new Color(r, g, b);
                    assertEquals(MapPalette.matchColor(color), craftMapColorCache.matchColor(color), String.format("Incorrect matched color c(%s, %s, %s)", color.getRed(), color.getGreen(), color.getBlue()));
                }
            }
        }
    }
}
