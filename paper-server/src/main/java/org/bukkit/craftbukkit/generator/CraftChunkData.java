/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bukkit.craftbukkit.generator;

import java.util.Arrays;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.material.MaterialData;

/**
 * Data to be used for the block types and data in a newly generated chunk.
 */
public final class CraftChunkData implements ChunkGenerator.ChunkData {
    private final int maxHeight;
    private final char[][] sections;

    public CraftChunkData(World world) {
        this(world.getMaxHeight());
    }

    /* pp for tests */ CraftChunkData(int maxHeight) {
        if (maxHeight > 256) {
            throw new IllegalArgumentException("World height exceeded max chunk height");
        }
        this.maxHeight = maxHeight;
        // Minecraft hardcodes this to 16 chunk sections.
        sections = new char[16][];
    }

    @Override
    public int getMaxHeight() {
        return maxHeight;
    }

    @Override
    public void setBlock(int x, int y, int z, Material material) {
        setBlock(x, y, z, material.getId());
    }

    @Override
    public void setBlock(int x, int y, int z, MaterialData material) {
        setBlock(x, y, z, material.getItemTypeId(), material.getData());
    }

    @Override
    public void setRegion(int xMin, int yMin, int zMin, int xMax, int yMax, int zMax, Material material) {
        setRegion(xMin, yMin, zMin, xMax, yMax, zMax, material.getId());
    }

    @Override
    public void setRegion(int xMin, int yMin, int zMin, int xMax, int yMax, int zMax, MaterialData material) {
        setRegion(xMin, yMin, zMin, xMax, yMax, zMax, material.getItemTypeId(), material.getData());
    }

    @Override
    public Material getType(int x, int y, int z) {
        return Material.getMaterial(getTypeId(x, y, z));
    }

    @Override
    public MaterialData getTypeAndData(int x, int y, int z) {
        return getType(x, y, z).getNewData(getData(x, y, z));
    }

    @Override
    public void setRegion(int xMin, int yMin, int zMin, int xMax, int yMax, int zMax, int blockId) {
        setRegion(xMin, yMin, zMin, xMax, yMax, zMax, blockId, (byte) 0);
    }

    @Override
    public void setRegion(int xMin, int yMin, int zMin, int xMax, int yMax, int zMax, int blockId, int data) {
        // Clamp to sane values.
        if (xMin > 0xf || yMin >= maxHeight || zMin > 0xf) {
            return;
        }
        if (xMin < 0) {
            xMin = 0;
        }
        if (yMin < 0) {
            yMin = 0;
        }
        if (zMin < 0) {
            zMin = 0;
        }
        if (xMax > 0x10) {
            xMax = 0x10;
        }
        if (yMax > maxHeight) {
            yMax = maxHeight;
        }
        if (zMax > 0x10) {
            zMax = 0x10;
        }
        if (xMin >= xMax || yMin >= yMax || zMin >= zMax) {
            return;
        }
        char typeChar = (char) ((blockId << 4) | data);
        if (xMin == 0 && xMax == 0x10) {
            if (zMin == 0 && zMax == 0x10) {
                for (int y = yMin & 0xf0; y < yMax; y += 0x10) {
                    char[] section = getChunkSection(y, true);
                    if (y <= yMin) {
                        if (y + 0x10 > yMax) {
                            // First and last chunk section
                            Arrays.fill(section, (yMin & 0xf) << 8, (yMax & 0xf) << 8, typeChar);
                        } else {
                            // First chunk section
                            Arrays.fill(section, (yMin & 0xf) << 8, 0x1000, typeChar);
                        }
                    } else if (y + 0x10 > yMax) {
                        // Last chunk section
                        Arrays.fill(section, 0, (yMax & 0xf) << 8, typeChar);
                    } else {
                        // Full chunk section
                        Arrays.fill(section, 0, 0x1000, typeChar);
                    }
                }
            } else {
                for (int y = yMin; y < yMax; y++) {
                    char[] section = getChunkSection(y, true);
                    int offsetBase = (y & 0xf) << 8;
                    int min = offsetBase | (zMin << 4);
                    // Need to add zMax as it can be 16, which overlaps the y coordinate bits
                    int max = offsetBase + (zMax << 4);
                    Arrays.fill(section, min, max, typeChar);
                }
            }
        } else {
            for (int y = yMin; y < yMax; y++) {
                char[] section = getChunkSection(y, true);
                int offsetBase = (y & 0xf) << 8;
                for (int z = zMin; z < zMax; z++) {
                    int offset = offsetBase | (z << 4);
                    // Need to add xMax as it can be 16, which overlaps the z coordinate bits
                    Arrays.fill(section, offset | xMin, offset + xMax, typeChar);
                }
            }
        }
    }

    @Override
    public void setBlock(int x, int y, int z, int blockId) {
        setBlock(x, y, z, blockId, (byte) 0);
    }

    @Override
    public void setBlock(int x, int y, int z, int blockId, byte data) {
        setBlock(x, y, z, (char) (blockId << 4 | data));
    }

    @Override
    public int getTypeId(int x, int y, int z) {
        if (x != (x & 0xf) || y < 0 || y >= maxHeight || z != (z & 0xf)) {
            return 0;
        }
        char[] section = getChunkSection(y, false);
        if (section == null) {
            return 0;
        } else {
            return section[(y & 0xf) << 8 | z << 4 | x] >> 4;
        }
    }

    @Override
    public byte getData(int x, int y, int z) {
        if (x != (x & 0xf) || y < 0 || y >= maxHeight || z != (z & 0xf)) {
            return (byte) 0;
        }
        char[] section = getChunkSection(y, false);
        if (section == null) {
            return (byte) 0;
        } else {
            return (byte) (section[(y & 0xf) << 8 | z << 4 | x] & 0xf);
        }
    }

    private void setBlock(int x, int y, int z, char type) {
        if (x != (x & 0xf) || y < 0 || y >= maxHeight || z != (z & 0xf)) {
            return;
        }
        char[] section = getChunkSection(y, true);
        section[(y & 0xf) << 8 | z << 4 | x] = type;
    }

    private char[] getChunkSection(int y, boolean create) {
        char[] section = sections[y >> 4];
        if (create && section == null) {
            sections[y >> 4] = section = new char[0x1000];
        }
        return section;
    }

    char[][] getRawChunkData() {
        return sections;
    }
}
