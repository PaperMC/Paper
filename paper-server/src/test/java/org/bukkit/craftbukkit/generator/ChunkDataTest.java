package org.bukkit.craftbukkit.generator;

import static org.junit.jupiter.api.Assertions.*;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.support.RegistryHelper;
import org.bukkit.support.environment.Legacy;
import org.junit.jupiter.api.Test;

@Legacy
public class ChunkDataTest {

    private static final BlockData RED_WOOL = Material.RED_WOOL.createBlockData();
    private static final BlockData AIR = Material.AIR.createBlockData();

    private boolean testSetBlock(OldCraftChunkData data, int x, int y, int z, BlockData type, BlockData expected) {
        data.setBlock(x, y, z, type);
        return expected.equals(data.getBlockData(x, y, z)) && expected.getMaterial().equals(data.getType(x, y, z));
    }

    private void testSetRegion(OldCraftChunkData data, int minx, int miny, int minz, int maxx, int maxy, int maxz, BlockData type) {
        data.setRegion(minx, miny, minz, maxx, maxy, maxz, type);
        for (int y = 0; y < data.getMaxHeight(); y++) {
            for (int z = 0; z < 16; z++) {
                for (int x = 0; x < 16; x++) {
                    boolean inRegion = miny <= y && y < maxy && minx <= x && x < maxx && minz <= z && z < maxz;
                    if (inRegion != type.equals(data.getBlockData(x, y, z))) {
                        throw new IllegalStateException(
                                "setRegion(" + minx + ", " + miny + ", " + minz + ", " + maxx + ", " + maxy + ", " + maxz + ", " + type + ")"
                                + "-> block at " + x + ", " + y + ", " + z + " is " + data.getBlockData(x, y, z));
                    }
                }
            }
        }
    }

    @Test
    public void testMinHeight() {
        OldCraftChunkData data = new OldCraftChunkData(-128, 128, RegistryHelper.palettedContainerFactory());
        assertTrue(this.testSetBlock(data, 0, -256, 0, ChunkDataTest.RED_WOOL, ChunkDataTest.AIR), "Could not set block below min height");
        assertTrue(this.testSetBlock(data, 0, -64, 0, ChunkDataTest.RED_WOOL, ChunkDataTest.RED_WOOL), "Could set block above min height");
    }

    @Test
    public void testMaxHeight() {
        OldCraftChunkData data = new OldCraftChunkData(0, 128, RegistryHelper.palettedContainerFactory());
        assertTrue(this.testSetBlock(data, 0, 128, 0, ChunkDataTest.RED_WOOL, ChunkDataTest.AIR), "Could not set block above max height");
        assertTrue(this.testSetBlock(data, 0, 127, 0, ChunkDataTest.RED_WOOL, ChunkDataTest.RED_WOOL), "Could set block below max height");
    }

    @Test
    public void testBoundsCheckingSingle() {
        OldCraftChunkData data = new OldCraftChunkData(0, 256, RegistryHelper.palettedContainerFactory());
        assertTrue(this.testSetBlock(data, 0, 0, 0, ChunkDataTest.RED_WOOL, ChunkDataTest.RED_WOOL), "Can set block inside chunk bounds");
        assertTrue(this.testSetBlock(data, 15, 255, 15, ChunkDataTest.RED_WOOL, ChunkDataTest.RED_WOOL), "Can set block inside chunk bounds");
        assertTrue(this.testSetBlock(data, -1, 0, 0, ChunkDataTest.RED_WOOL, ChunkDataTest.AIR), "Can no set block outside chunk bounds");
        assertTrue(this.testSetBlock(data, 0, -1, 0, ChunkDataTest.RED_WOOL, ChunkDataTest.AIR), "Can no set block outside chunk bounds");
        assertTrue(this.testSetBlock(data, 0, 0, -1, ChunkDataTest.RED_WOOL, ChunkDataTest.AIR), "Can no set block outside chunk bounds");
        assertTrue(this.testSetBlock(data, 16, 0, 0, ChunkDataTest.RED_WOOL, ChunkDataTest.AIR), "Can no set block outside chunk bounds");
        assertTrue(this.testSetBlock(data, 0, 256, 0, ChunkDataTest.RED_WOOL, ChunkDataTest.AIR), "Can no set block outside chunk bounds");
        assertTrue(this.testSetBlock(data, 0, 0, 16, ChunkDataTest.RED_WOOL, ChunkDataTest.AIR), "Can no set block outside chunk bounds");
    }

    @Test
    public void testSetRegion() {
        OldCraftChunkData data = new OldCraftChunkData(0, 256, RegistryHelper.palettedContainerFactory());
        this.testSetRegion(data, -100, 0, -100, 0, 256, 0, ChunkDataTest.RED_WOOL); // exclusively outside
        this.testSetRegion(data, 16, 256, 16, 0, 0, 0, ChunkDataTest.RED_WOOL); // minimum >= maximum
        this.testSetRegion(data, 0, 0, 0, 0, 0, 0, ChunkDataTest.RED_WOOL); // minimum == maximum
        this.testSetRegion(data, 0, 0, 0, 16, 16, 16, ChunkDataTest.RED_WOOL); // Whole Chunk Section
        data.setRegion(0, 0, 0, 16, 256, 16, ChunkDataTest.AIR);
        this.testSetRegion(data, 0, 8, 0, 16, 24, 16, ChunkDataTest.RED_WOOL); // Start middle of this section, end middle of next
        data.setRegion(0, 0, 0, 16, 256, 16, ChunkDataTest.AIR);
        this.testSetRegion(data, 0, 4, 0, 16, 12, 16, ChunkDataTest.RED_WOOL); // Start in this section, end in this section
        data.setRegion(0, 0, 0, 16, 256, 16, ChunkDataTest.AIR);
        this.testSetRegion(data, 0, 0, 0, 16, 16, 1, ChunkDataTest.RED_WOOL); // Whole Chunk Section
        data.setRegion(0, 0, 0, 16, 256, 16, ChunkDataTest.AIR);
        this.testSetRegion(data, 0, 8, 0, 16, 24, 1, ChunkDataTest.RED_WOOL); // Start middle of this section, end middle of next
        data.setRegion(0, 0, 0, 16, 256, 16, ChunkDataTest.AIR);
        this.testSetRegion(data, 0, 4, 0, 16, 12, 1, ChunkDataTest.RED_WOOL); // Start in this section, end in this section
        data.setRegion(0, 0, 0, 16, 256, 16, ChunkDataTest.AIR);
        this.testSetRegion(data, 0, 0, 0, 1, 16, 1, ChunkDataTest.RED_WOOL); // Whole Chunk Section
        data.setRegion(0, 0, 0, 16, 256, 16, ChunkDataTest.AIR);
        this.testSetRegion(data, 0, 8, 0, 1, 24, 1, ChunkDataTest.RED_WOOL); // Start middle of this section, end middle of next
        data.setRegion(0, 0, 0, 16, 256, 16, ChunkDataTest.AIR);
        this.testSetRegion(data, 0, 4, 0, 1, 12, 1, ChunkDataTest.RED_WOOL); // Start in this section, end in this section
        data.setRegion(0, 0, 0, 16, 256, 16, ChunkDataTest.AIR);
        this.testSetRegion(data, 0, 0, 0, 1, 1, 1, ChunkDataTest.RED_WOOL); // Set single block.
    }
}
