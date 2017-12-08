package org.bukkit.craftbukkit.generator;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Wool;
import org.junit.Test;
import static org.junit.Assert.*;

public class ChunkDataTest {

    private static final MaterialData RED_WOOL = new Wool(DyeColor.RED);
    private static final MaterialData AIR = new MaterialData(Material.AIR);

    private boolean testSetBlock(CraftChunkData data, int x, int y, int z, MaterialData type, MaterialData expected) {
        data.setBlock(x, y, z, type);
        return expected.equals(data.getTypeAndData(x, y, z));
    }

    private void testSetRegion(CraftChunkData data, int minx, int miny, int minz, int maxx, int maxy, int maxz, MaterialData type) {
        data.setRegion(minx, miny, minz, maxx, maxy, maxz, type);
        for (int y = 0; y < data.getMaxHeight(); y++) {
            for (int z = 0; z < 16; z++) {
                for (int x = 0; x < 16; x++) {
                    boolean inRegion = miny <= y && y < maxy && minx <= x && x < maxx && minz <= z && z < maxz;
                    if (inRegion != type.equals(data.getTypeAndData(x, y, z))) {
                        throw new IllegalStateException(
                                "setRegion(" + minx + ", " + miny + ", " + minz + ", " + maxx + ", " + maxy + ", " + maxz + ", " + type + ")"
                                + "-> block at " + x + ", " + y + ", " + z + " is " + data.getTypeAndData(x, y, z));
                    }
                }
            }
        }
    }

    @Test
    public void testMaxHeight() {
        CraftChunkData data = new CraftChunkData(128);
        assertTrue("Could not set block above max height", testSetBlock(data, 0, 128, 0, RED_WOOL, AIR));
        assertTrue("Could set block below max height", testSetBlock(data, 0, 127, 0, RED_WOOL, RED_WOOL));
    }

    @Test
    public void testBoundsCheckingSingle() {
        CraftChunkData data = new CraftChunkData(256);
        assertTrue("Can set block inside chunk bounds", testSetBlock(data, 0, 0, 0, RED_WOOL, RED_WOOL));
        assertTrue("Can set block inside chunk bounds", testSetBlock(data, 15, 255, 15, RED_WOOL, RED_WOOL));
        assertTrue("Can no set block outside chunk bounds", testSetBlock(data, -1, 0, 0, RED_WOOL, AIR));
        assertTrue("Can no set block outside chunk bounds", testSetBlock(data, 0, -1, 0, RED_WOOL, AIR));
        assertTrue("Can no set block outside chunk bounds", testSetBlock(data, 0, 0, -1, RED_WOOL, AIR));
        assertTrue("Can no set block outside chunk bounds", testSetBlock(data, 16, 0, 0, RED_WOOL, AIR));
        assertTrue("Can no set block outside chunk bounds", testSetBlock(data, 0, 256, 0, RED_WOOL, AIR));
        assertTrue("Can no set block outside chunk bounds", testSetBlock(data, 0, 0, 16, RED_WOOL, AIR));
    }

    @Test
    public void testSetRegion() {
        CraftChunkData data = new CraftChunkData(256);
        testSetRegion(data, -100, 0, -100, 0, 256, 0, RED_WOOL); // exclusively outside
        testSetRegion(data, 16, 256, 16, 0, 0, 0, RED_WOOL); // minimum >= maximum
        testSetRegion(data, 0, 0, 0, 0, 0, 0, RED_WOOL); // minimum == maximum
        testSetRegion(data, 0, 0, 0, 16, 16, 16, RED_WOOL); // Whole Chunk Section
        data.setRegion(0, 0, 0, 16, 256, 16, AIR);
        testSetRegion(data, 0, 8, 0, 16, 24, 16, RED_WOOL); // Start middle of this section, end middle of next 
        data.setRegion(0, 0, 0, 16, 256, 16, AIR);
        testSetRegion(data, 0, 4, 0, 16, 12, 16, RED_WOOL); // Start in this section, end in this section
        data.setRegion(0, 0, 0, 16, 256, 16, AIR);
        testSetRegion(data, 0, 0, 0, 16, 16, 1, RED_WOOL); // Whole Chunk Section
        data.setRegion(0, 0, 0, 16, 256, 16, AIR);
        testSetRegion(data, 0, 8, 0, 16, 24, 1, RED_WOOL); // Start middle of this section, end middle of next 
        data.setRegion(0, 0, 0, 16, 256, 16, AIR);
        testSetRegion(data, 0, 4, 0, 16, 12, 1, RED_WOOL); // Start in this section, end in this section
        data.setRegion(0, 0, 0, 16, 256, 16, AIR);
        testSetRegion(data, 0, 0, 0, 1, 16, 1, RED_WOOL); // Whole Chunk Section
        data.setRegion(0, 0, 0, 16, 256, 16, AIR);
        testSetRegion(data, 0, 8, 0, 1, 24, 1, RED_WOOL); // Start middle of this section, end middle of next 
        data.setRegion(0, 0, 0, 16, 256, 16, AIR);
        testSetRegion(data, 0, 4, 0, 1, 12, 1, RED_WOOL); // Start in this section, end in this section
        data.setRegion(0, 0, 0, 16, 256, 16, AIR);
        testSetRegion(data, 0, 0, 0, 1, 1, 1, RED_WOOL); // Set single block.
    }
}
