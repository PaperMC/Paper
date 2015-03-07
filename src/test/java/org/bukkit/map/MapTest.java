
package org.bukkit.map;

import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.server.MaterialMapColor;
import org.junit.Assert;
import org.junit.Test;

public class MapTest {

    private static final Logger logger = Logger.getLogger("MapTest");

    private static final int[] modifiers = {180, 220, 255, 135};

    @Test
    public void testColors() {
        MaterialMapColor[] nmsColors = MaterialMapColor.a;
        Color[] bukkitColors = MapPalette.colors;

        boolean fail = false;
        for (int i = 0; i < nmsColors.length; i++) {
            if (nmsColors[i] == null) {
                break;
            }
            int rgb = nmsColors[i].L;

            int r = (rgb >> 16) & 0xFF;
            int g = (rgb >> 8) & 0xFF;
            int b = rgb & 0xFF;

            if (i > bukkitColors.length/4) {
                for (int modi : modifiers) {
                    int mr = (r * modi) / 255;
                    int mg = (g * modi) / 255;
                    int mb = (b * modi) / 255;
                    logger.log(Level.WARNING, "Missing color: c({0}, {1}, {2})", new Object[]{mr, mg, mb});
                }
                fail = true;
            } else {
                for (int j = 0; j < modifiers.length; j++) {
                    int modi = modifiers[j];
                    Color bukkit = bukkitColors[i * 4 + j];
                    int mr = (r * modi) / 255;
                    int mg = (g * modi) / 255;
                    int mb = (b * modi) / 255;

                    if (bukkit.getRed() != mr || bukkit.getGreen() != mg || bukkit.getBlue() != mb) {
                        logger.log(Level.WARNING, "Incorrect color: {6} {7} c({0}, {1}, {2}) != c({3}, {4}, {5})", new Object[]{
                            bukkit.getRed(), bukkit.getGreen(), bukkit.getBlue(),
                            mr, mg, mb,
                            i, j
                        });
                        fail = true;
                    }
                }
            }
        }
        Assert.assertFalse(fail);
    }
}
