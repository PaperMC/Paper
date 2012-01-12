package org.bukkit.craftbukkit.block;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.bukkit.block.BlockFace;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.PistonMoveReaction;

import net.minecraft.server.BiomeBase;
import net.minecraft.server.BlockRedstoneWire;
import net.minecraft.server.EnumSkyBlock;

import org.bukkit.*;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.CraftChunk;
import org.bukkit.inventory.ItemStack;
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

    public World getWorld() {
        return chunk.getWorld();
    }

    public Location getLocation() {
        return new Location(getWorld(), x, y, z);
    }

    public BlockVector getVector() {
        return new BlockVector(x, y, z);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public Chunk getChunk() {
        return chunk;
    }

    public void setData(final byte data) {
        chunk.getHandle().world.setData(x, y, z, data);
    }

    public void setData(final byte data, boolean applyPhysics) {
        if (applyPhysics) {
            chunk.getHandle().world.setData(x, y, z, data);
        } else {
            chunk.getHandle().world.setRawData(x, y, z, data);
        }
    }

    public byte getData() {
        return (byte) chunk.getHandle().getData(this.x & 0xF, this.y & 0x7F, this.z & 0xF);
    }

    public void setType(final Material type) {
        setTypeId(type.getId());
    }

    public boolean setTypeId(final int type) {
        return chunk.getHandle().world.setTypeId(x, y, z, type);
    }

    public boolean setTypeId(final int type, final boolean applyPhysics) {
        if (applyPhysics) {
            return setTypeId(type);
        } else {
            return chunk.getHandle().world.setRawTypeId(x, y, z, type);
        }
    }

    public boolean setTypeIdAndData(final int type, final byte data, final boolean applyPhysics) {
        if (applyPhysics) {
            return chunk.getHandle().world.setTypeIdAndData(x, y, z, type, data);
        } else {
            boolean success = chunk.getHandle().world.setRawTypeIdAndData(x, y, z, type, data);
            if (success) {
                chunk.getHandle().world.notify(x, y, z);
            }
            return success;
        }
    }

    public Material getType() {
        return Material.getMaterial(getTypeId());
    }

    public int getTypeId() {
        return chunk.getHandle().getTypeId(this.x & 0xF, this.y & 0x7F, this.z & 0xF);
    }

    public byte getLightLevel() {
        return (byte) chunk.getHandle().world.getLightLevel(this.x, this.y, this.z);
    }

    public byte getLightFromSky() {
        return (byte) chunk.getHandle().a(EnumSkyBlock.SKY, this.x & 0xF, this.y & 0x7F, this.z & 0xF);
    }

    public byte getLightFromBlocks() {
        return (byte) chunk.getHandle().a(EnumSkyBlock.BLOCK, this.x & 0xF, this.y & 0x7F, this.z & 0xF);
    }


    public Block getFace(final BlockFace face) {
        return getRelative(face, 1);
    }

    public Block getFace(final BlockFace face, final int distance) {
        return getRelative(face, distance);
    }

    public Block getRelative(final int modX, final int modY, final int modZ) {
        return getWorld().getBlockAt(getX() + modX, getY() + modY, getZ() + modZ);
    }

    public Block getRelative(BlockFace face) {
        return getRelative(face, 1);
    }

    public Block getRelative(BlockFace face, int distance) {
        return getRelative(face.getModX() * distance, face.getModY() * distance, face.getModZ() * distance);
    }

    public BlockFace getFace(final Block block) {
        BlockFace[] values = BlockFace.values();

        for (BlockFace face : values) {
            if ((this.getX() + face.getModX() == block.getX()) &&
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
        return "CraftBlock{" + "chunk=" + chunk + ",x=" + x + ",y=" + y + ",z=" + z + ",type=" + getType() + ",data=" + getData() + '}';
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
        switch (face) {
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
            return 7; // Good as anything here, but technically invalid
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
        case JUKEBOX:
            return new CraftJukebox(this);
        default:
            return new CraftBlockState(this);
        }
    }

    public Biome getBiome() {
        return getWorld().getBiome(x, z);
    }

    public static Biome biomeBaseToBiome(BiomeBase base) {
        if (base == BiomeBase.SWAMPLAND) {
            return Biome.SWAMPLAND;
        } else if (base == BiomeBase.FOREST) {
            return Biome.FOREST;
        } else if (base == BiomeBase.TAIGA) {
            return Biome.TAIGA;
        } else if (base == BiomeBase.DESERT) {
            return Biome.DESERT;
        } else if (base == BiomeBase.PLAINS) {
            return Biome.PLAINS;
        } else if (base == BiomeBase.HELL) {
            return Biome.HELL;
        } else if (base == BiomeBase.SKY) {
            return Biome.SKY;
        } else if (base == BiomeBase.RIVER) {
            return Biome.RIVER;
        } else if (base == BiomeBase.EXTREME_HILLS) {
            return Biome.EXTREME_HILLS;
        } else if (base == BiomeBase.OCEAN) {
            return Biome.OCEAN;
        } else if (base == BiomeBase.FROZEN_OCEAN) {
            return Biome.FROZEN_OCEAN;
        } else if (base == BiomeBase.FROZEN_RIVER) {
            return Biome.FROZEN_RIVER;
        } else if (base == BiomeBase.ICE_PLAINS) {
            return Biome.ICE_PLAINS;
        } else if (base == BiomeBase.ICE_MOUNTAINS) {
            return Biome.ICE_MOUNTAINS;
        } else if (base == BiomeBase.MUSHROOM_ISLAND) {
            return Biome.MUSHROOM_ISLAND;
        } else if (base == BiomeBase.MUSHROOM_SHORE) {
            return Biome.MUSHROOM_SHORE;
        } else if (base == BiomeBase.BEACH) {
            return Biome.BEACH;
        } else if (base == BiomeBase.DESERT_HILLS) {
            return Biome.DESERT_HILLS;
        } else if (base == BiomeBase.FOREST_HILLS) {
            return Biome.FOREST_HILLS;
        } else if (base == BiomeBase.TAIGA_HILLS) {
            return Biome.TAIGA_HILLS;
        } else if (base == BiomeBase.SMALL_MOUNTAINS) {
            return Biome.SMALL_MOUNTAINS;
        }

        return null;
    }

    public double getTemperature() {
        return getWorld().getTemperature(x, z);
    }

    public double getHumidity() {
        return getWorld().getHumidity(x, z);
    }

    public boolean isBlockPowered() {
        return chunk.getHandle().world.isBlockPowered(x, y, z);
    }

    public boolean isBlockIndirectlyPowered() {
        return chunk.getHandle().world.isBlockIndirectlyPowered(x, y, z);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof CraftBlock)) return false;
        CraftBlock other = (CraftBlock) o;

        return this.x == other.x && this.y == other.y && this.z == other.z && this.getWorld().equals(other.getWorld());
    }

    @Override
    public int hashCode() {
        return this.y << 24 ^ this.x ^ this.z ^ this.getWorld().hashCode();
    }

    public boolean isBlockFacePowered(BlockFace face) {
        return chunk.getHandle().world.isBlockFacePowered(x, y, z, blockFaceToNotch(face));
    }

    public boolean isBlockFaceIndirectlyPowered(BlockFace face) {
        return chunk.getHandle().world.isBlockFaceIndirectlyPowered(x, y, z, blockFaceToNotch(face));
    }

    public int getBlockPower(BlockFace face) {
        int power = 0;
        BlockRedstoneWire wire = (BlockRedstoneWire) net.minecraft.server.Block.REDSTONE_WIRE;
        net.minecraft.server.World world = chunk.getHandle().world;
        if ((face == BlockFace.DOWN || face == BlockFace.SELF) && world.isBlockFacePowered(x, y - 1, z, 0)) power = wire.getPower(world, x, y - 1, z, power);
        if ((face == BlockFace.UP || face == BlockFace.SELF) && world.isBlockFacePowered(x, y + 1, z, 1)) power = wire.getPower(world, x, y + 1, z, power);
        if ((face == BlockFace.EAST || face == BlockFace.SELF) && world.isBlockFacePowered(x, y, z - 1, 2)) power = wire.getPower(world, x, y, z - 1, power);
        if ((face == BlockFace.WEST || face == BlockFace.SELF) && world.isBlockFacePowered(x, y, z + 1, 3)) power = wire.getPower(world, x, y, z + 1, power);
        if ((face == BlockFace.NORTH || face == BlockFace.SELF) && world.isBlockFacePowered(x - 1, y, z, 4)) power = wire.getPower(world, x - 1, y, z, power);
        if ((face == BlockFace.SOUTH || face == BlockFace.SELF) && world.isBlockFacePowered(x + 1, y, z, 5)) power = wire.getPower(world, x + 1, y, z, power);
        return power > 0 ? power : (face == BlockFace.SELF ? isBlockIndirectlyPowered() : isBlockFaceIndirectlyPowered(face)) ? 15 : 0;
    }

    public int getBlockPower() {
        return getBlockPower(BlockFace.SELF);
    }

    public boolean isEmpty() {
        return getType() == Material.AIR;
    }

    public boolean isLiquid() {
        return (getType() == Material.WATER) || (getType() == Material.STATIONARY_WATER) || (getType() == Material.LAVA) || (getType() == Material.STATIONARY_LAVA);
    }

    public PistonMoveReaction getPistonMoveReaction() {
        return PistonMoveReaction.getById(net.minecraft.server.Block.byId[this.getTypeId()].material.getPushReaction());
    }

    private boolean itemCausesDrops(ItemStack item) {
        net.minecraft.server.Block block = net.minecraft.server.Block.byId[this.getTypeId()];
        net.minecraft.server.Item itemType = item != null ? net.minecraft.server.Item.byId[item.getTypeId()] : null;
        return block != null && (block.material.k() || (itemType != null && itemType.a(block)));
    }

    public boolean breakNaturally() {
        net.minecraft.server.Block block = net.minecraft.server.Block.byId[this.getTypeId()];
        byte data = getData();

        setTypeId(Material.AIR.getId());
        if (block != null) {
            block.dropNaturally(chunk.getHandle().world, x, y, z, data, 1.0F, 0);
            return true;
        }
        return false;
    }

    public boolean breakNaturally(ItemStack item) {
        if (itemCausesDrops(item)) {
            return breakNaturally();
        } else {
            return setTypeId(Material.AIR.getId());
        }
    }

    public Collection<ItemStack> getDrops() {
        List<ItemStack> drops = new ArrayList<ItemStack>();

        net.minecraft.server.Block block = net.minecraft.server.Block.byId[this.getTypeId()];
        if (block != null) {
            byte data = getData();
            // based on nms.Block.dropNaturally
            int count = block.getDropCount(0, chunk.getHandle().world.random);
            for (int i = 0; i < count; ++i) {
                int item = block.getDropType(data, chunk.getHandle().world.random, 0);
                if (item > 0) {
                    drops.add(new ItemStack(item, 1, (short) net.minecraft.server.Block.getDropData(block, data)));
                }
            }
        }
        return drops;
    }

    public Collection<ItemStack> getDrops(ItemStack item) {
        if (itemCausesDrops(item)) {
            return getDrops();
        } else {
            return Collections.emptyList();
        }
    }
}
