package org.bukkit.craftbukkit.block;

import org.bukkit.block.BlockFace;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;

import net.minecraft.server.BiomeBase;
import net.minecraft.server.BlockRedstoneWire;
import org.bukkit.*;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.CraftChunk;
import org.bukkit.util.BlockVector;

public class CraftBlock implements Block {
    private final CraftChunk chunk;
    private final int x;
    private final int y;
    private final int z;

    public CraftBlock(CraftChunk chunk, int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.chunk = chunk;
    }

    /**
     * Gets the world which contains this Block
     *
     * @return World containing this block
     */
    public World getWorld() {
        return chunk.getWorld();
    }

    /**
     * Gets the Location of the block
     *
     * @return Location of the block
     */
    public Location getLocation() {
      return new Location(getWorld(), x, y, z);
    }

    public BlockVector getVector() {
        return new BlockVector(x, y, z);
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
        chunk.getHandle().d.c(x, y, z, data);
    }

    public void setData(final byte data, boolean applyPhysics) {
        if (applyPhysics) {
            chunk.getHandle().d.c(x, y, z, data);
        } else {
            chunk.getHandle().d.d(x, y, z, data);
        }
    }

    /**
     * Gets the metadata for this block
     *
     * @return block specific metadata
     */
    public byte getData() {
        return (byte) chunk.getHandle().b(this.x & 0xF, this.y & 0x7F, this.z & 0xF);
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
        return chunk.getHandle().d.e(x, y, z, type);
    }
    
    public boolean setTypeId(final int type, final boolean applyPhysics) {
        if (applyPhysics) {
            return setTypeId(type);
        } else {
            return chunk.getHandle().d.setTypeId(x, y, z, type);
        }
    }

    public boolean setTypeIdAndData(final int type, final byte data, final boolean applyPhysics) {
        if (applyPhysics) {
            return chunk.getHandle().d.b(x, y, z, type, data);
        } else {
            return chunk.getHandle().d.setTypeIdAndData(x, y, z, type, data);
        }
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
        return chunk.getHandle().a(this.x & 0xF, this.y & 0x7F, this.z & 0xF);
    }

    /**
     * Gets the light level between 0-15
     *
     * @return light level
     */
    public byte getLightLevel() {
        return (byte) chunk.getHandle().d.j(this.x, this.y, this.z);
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
        return getRelative(face.getModX() * distance, face.getModY() * distance, face.getModZ() * distance);
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
        return "CraftBlock{" + "chunk=" + chunk + "x=" + x + "y=" + y + "z=" + z + '}';
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
    
    public static int blockFaceToNotch(BlockFace face) {
        switch(face) {
            case DOWN:
                return 0;
            case UP:
                return 1;
            case EAST:
                return 2;
            case WEST:
                return 3;
            case NORTH:
                return 4;
            case SOUTH:
                return 5;
            default:
                return 7; //Good as anything here, but technically invalid
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
                return new CraftCreatureSpawner(this);
            case NOTE_BLOCK:
                return new CraftNoteBlock(this);
            default:
                return new CraftBlockState(this);
        }
    }

    public Biome getBiome() {
        BiomeBase base = chunk.getHandle().d.a().a(chunk.getX(), chunk.getZ());

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
        return chunk.getHandle().d.o(x, y, z);
    }

    public boolean isBlockIndirectlyPowered() {
        return chunk.getHandle().d.p(x, y, z);
    }

    @Override
    public boolean equals( Object o ) {
        return this == o;
    }

    public boolean isBlockFacePowered(BlockFace face) {
        return chunk.getHandle().d.i(x, y, z, blockFaceToNotch(face));
    }

    public boolean isBlockFaceIndirectlyPowered(BlockFace face) {
        return chunk.getHandle().d.j(x, y, z, blockFaceToNotch(face));
    }
    
    public int getBlockPower(BlockFace face) {
        int power = 0;
        BlockRedstoneWire wire = (BlockRedstoneWire) net.minecraft.server.Block.REDSTONE_WIRE;
        net.minecraft.server.World world = chunk.getHandle().d;
        if ((face == BlockFace.DOWN || face == BlockFace.SELF) && world.i(x, y - 1, z, 0)) power = wire.g(world, x, y - 1, z, power);
        if ((face == BlockFace.UP || face == BlockFace.SELF) && world.i(x, y + 1, z, 1)) power = wire.g(world, x, y + 1, z, power);
        if ((face == BlockFace.EAST || face == BlockFace.SELF) && world.i(x, y, z - 1, 2)) power = wire.g(world, x, y, z - 1, power);
        if ((face == BlockFace.WEST || face == BlockFace.SELF) && world.i(x, y, z + 1, 3)) power = wire.g(world, x, y, z + 1, power);
        if ((face == BlockFace.NORTH || face == BlockFace.SELF) && world.i(x - 1, y, z, 4)) power = wire.g(world, x - 1, y, z, power);
        if ((face == BlockFace.SOUTH || face == BlockFace.SELF) && world.i(x + 1, y, z, 5)) power = wire.g(world, x + 1, y, z, power);
        return face == BlockFace.SELF ? power - 1 : power;
    }
    
    public int getBlockPower() {
        return getBlockPower(BlockFace.SELF);
    }
}
