
package org.bukkit.craftbukkit.block;

import org.bukkit.block.BlockFace;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.Location;

import net.minecraft.server.BiomeBase;
import org.bukkit.*;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.CraftChunk;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.block.CraftBlockState;
import org.bukkit.craftbukkit.block.CraftSign;

public class CraftBlock implements Block {
    private final CraftWorld world;
    private final CraftChunk chunk;
    private final int x;
    private final int y;
    private final int z;
    protected int type;
    protected byte data;
    protected byte light;

    public CraftBlock(final CraftWorld world, final int x, final int y, final int z, final int type, final byte data) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.type = type;
        this.data = data;
        this.light = (byte)world.getHandle().j(x, y, z);
        this.chunk = (CraftChunk)world.getChunkAt(x >> 4, z >> 4);
    }

    protected CraftBlock(final CraftWorld world, final int x, final int y,
            final int z, final int type, final byte data, final byte light) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.type = type;
        this.data = data;
        this.light = light;
        this.chunk = (CraftChunk)world.getChunkAt(x >> 4, z >> 4);
    }

    /**
     * Gets the world which contains this Block
     *
     * @return World containing this block
     */
    public World getWorld() {
        return world;
    }

    /**
     * Gets the Location of the block
     *
     * @return Location of the block
     */
    public Location getLocation() {
      return new Location(world, x, y, z);
    }

    /**
     * Gets the x-coordinate of this block
     *
     * @return x-coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the y-coordinate of this block
     *
     * @return y-coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Gets the z-coordinate of this block
     *
     * @return z-coordinate
     */
    public int getZ() {
        return z;
    }

    /**
     * Gets the chunk which contains this block
     *
     * @return Containing Chunk
     */
    public Chunk getChunk() {
        return chunk;
    }

    /**
     * Sets the metadata for this block
     *
     * @param data New block specific metadata
     */
    public void setData(final byte data) {
        this.data = data;
        world.getHandle().c(x, y, z, data);
    }

    /**
     * Gets the metadata for this block
     *
     * @return block specific metadata
     */
    public byte getData() {
        return data;
    }

    /**
     * Sets the type of this block
     *
     * @param type Material to change this block to
     */
    public void setType(final Material type) {
        setTypeId(type.getId());
    }

    /**
     * Sets the type-id of this block
     *
     * @param type Type-Id to change this block to
     * @return whether the block was changed
     */
    public boolean setTypeId(final int type) {
        this.type = type;
        return world.getHandle().e(x, y, z, type);
    }

    /**
     * Gets the type of this block
     *
     * @return block type
     */
    public Material getType() {
        return Material.getMaterial(getTypeId());
    }

    /**
     * Gets the type-id of this block
     *
     * @return block type-id
     */
    public int getTypeId() {
        return type;
    }
    
    /**
     * Gets the light level between 0-15
     * 
     * @return light level
     */
    public byte getLightLevel() {
        return light;
    }

    /**
     * Gets the block at the given face
     *
     * @param face Face of this block to return
     * @return Block at the given face
     */
    public Block getFace(final BlockFace face) {
        return getFace(face, 1);
    }

    /**
     * Gets the block at the given distance of the given face<br />
     * <br />
     * For example, the following method places water at 100,102,100; two blocks
     * above 100,100,100.
     * <pre>
     * Block block = world.getBlockAt(100,100,100);
     * Block shower = block.getFace(BlockFace.UP, 2);
     * shower.setType(Material.WATER);
     * </pre>
     *
     * @param face Face of this block to return
     * @param distance Distance to get the block at
     * @return Block at the given face
     */
    public Block getFace(final BlockFace face, final int distance) {
        return getRelative(face.getModX() * distance, face.getModY() * distance,
                face.getModZ() * distance);
    }

    /**
     * Gets the block at the given offsets
     *
     * @param modX X-coordinate offset
     * @param modY Y-coordinate offset
     * @param modZ Z-coordinate offset
     * @return Block at the given offsets
     */
    public Block getRelative(final int modX, final int modY, final int modZ) {
        return getWorld().getBlockAt(getX() + modX, getY() + modY, getZ() + modZ);
    }

    /**
     * Gets the block at the given offsets
     *
     * @param face face
     * @return Block at the given offsets
     */
    public Block getRelative(BlockFace face) {
        return getRelative(face.getModX(), face.getModY(), face.getModZ());
    }

    /**
     * Gets the face relation of this block compared to the given block<br />
     * <br />
     * For example:
     * <pre>
     * Block current = world.getBlockAt(100, 100, 100);
     * Block target = world.getBlockAt(100, 101, 100);
     *
     * current.getFace(target) == BlockFace.UP;
     * </pre>
     * <br />
     * If the given block is not connected to this block, null may be returned
     *
     * @param block Block to compare against this block
     * @return BlockFace of this block which has the requested block, or null
     */
    public BlockFace getFace(final Block block) {
        BlockFace[] values = BlockFace.values();

        for (BlockFace face : values) {
            if (
                    (this.getX() + face.getModX() == block.getX()) &&
                    (this.getY() + face.getModY() == block.getY()) &&
                    (this.getZ() + face.getModZ() == block.getZ())
                ) {
                return face;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return "CraftBlock{" + "world=" + world + "x=" + x + "y=" + y + "z=" + z + "type=" + type + "data=" + data + '}';
    }
    
    /**
     * Notch uses a 0-5 to mean DOWN, UP, EAST, WEST, NORTH, SOUTH
     * in that order all over. This method is convenience to convert for us.
     * 
     * @return BlockFace the BlockFace represented by this number
     */
    public static BlockFace notchToBlockFace(int notch) {
        switch (notch) {
        case 0:
            return BlockFace.DOWN;
        case 1:
            return BlockFace.UP;
        case 2:
            return BlockFace.EAST;
        case 3:
            return BlockFace.WEST;
        case 4:
            return BlockFace.NORTH;
        case 5:
            return BlockFace.SOUTH;
        default:
            return BlockFace.SELF;
        }
    }

    public BlockState getState() {
        Material material = getType();

        switch (material) {
            case SIGN:
            case SIGN_POST:
            case WALL_SIGN:
                return new CraftSign(this);
            case CHEST:
                return new CraftChest(this);
            case BURNING_FURNACE:
            case FURNACE:
                return new CraftFurnace(this);
            case DISPENSER:
                return new CraftDispenser(this);
            case MOB_SPAWNER:
                return new CraftMobSpawner(this);
            case NOTE_BLOCK:
                return new CraftNoteBlock(this);
            default:
                return new CraftBlockState(this);
        }
    }

    public Biome getBiome() {
        // TODO: This may not be 100% accurate; investigate into getting per-block instead of per-chunk
        BiomeBase base = world.getHandle().a().a(chunk.getX(), chunk.getZ());

        if (base == BiomeBase.RAINFOREST) {
            return Biome.RAINFOREST;
        } else if (base == BiomeBase.SWAMPLAND) {
            return Biome.SWAMPLAND;
        } else if (base == BiomeBase.SEASONAL_FOREST) {
            return Biome.SEASONAL_FOREST;
        } else if (base == BiomeBase.FOREST) {
            return Biome.FOREST;
        } else if (base == BiomeBase.SAVANNA) {
            return Biome.SAVANNA;
        } else if (base == BiomeBase.SHRUBLAND) {
            return Biome.SHRUBLAND;
        } else if (base == BiomeBase.TAIGA) {
            return Biome.TAIGA;
        } else if (base == BiomeBase.DESERT) {
            return Biome.DESERT;
        } else if (base == BiomeBase.PLAINS) {
            return Biome.PLAINS;
        } else if (base == BiomeBase.ICE_DESERT) {
            return Biome.ICE_DESERT;
        } else if (base == BiomeBase.TUNDRA) {
            return Biome.TUNDRA;
        } else if (base == BiomeBase.HELL) {
            return Biome.HELL;
        }

        return null;
    }

    public boolean isBlockPowered() {
        return world.getHandle().o(x, y, z);
    }

    public boolean isBlockIndirectlyPowered() {
        return world.getHandle().p(x, y, z);
    }

    public void update() {
        type = world.getHandle().getTypeId(x, y, z);
        data = (byte)world.getHandle().getData(x, y, z);
        light = (byte)world.getHandle().j(x, y, z);
    }
}
