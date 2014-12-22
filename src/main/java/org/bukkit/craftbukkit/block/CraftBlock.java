package org.bukkit.craftbukkit.block;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.minecraft.server.*;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.craftbukkit.CraftChunk;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.BlockVector;

public class CraftBlock implements Block {
    private final CraftChunk chunk;
    private final int x;
    private final int y;
    private final int z;
    private static final Biome BIOME_MAPPING[];
    private static final BiomeBase BIOMEBASE_MAPPING[];

    public CraftBlock(CraftChunk chunk, int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.chunk = chunk;
    }

    private net.minecraft.server.Block getNMSBlock() {
        return CraftMagicNumbers.getBlock(this); // TODO: UPDATE THIS
    }

    private static net.minecraft.server.Block getNMSBlock(int type) {
        return CraftMagicNumbers.getBlock(type);
    }

    public World getWorld() {
        return chunk.getWorld();
    }

    public Location getLocation() {
        return new Location(getWorld(), x, y, z);
    }

    public Location getLocation(Location loc) {
        if (loc != null) {
            loc.setWorld(getWorld());
            loc.setX(x);
            loc.setY(y);
            loc.setZ(z);
            loc.setYaw(0);
            loc.setPitch(0);
        }

        return loc;
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
        setData(data, 3);
    }

    public void setData(final byte data, boolean applyPhysics) {
        if (applyPhysics) {
            setData(data, 3);
        } else {
            setData(data, 2);
        }
    }

    private void setData(final byte data, int flag) {
        net.minecraft.server.World world = chunk.getHandle().getWorld();
        BlockPosition position = new BlockPosition(x, y, z);
        IBlockData blockData = world.getType(position);
        world.setTypeAndData(position, blockData.getBlock().fromLegacyData(data), flag);
    }

    public byte getData() {
        IBlockData blockData = chunk.getHandle().getBlockData(new BlockPosition(x, y, z));
        return (byte) blockData.getBlock().toLegacyData(blockData);
    }

    public void setType(final Material type) {
        setType(type, true);
    }

    @Override
    public void setType(Material type, boolean applyPhysics) {
        setTypeId(type.getId(), applyPhysics);
    }

    public boolean setTypeId(final int type) {
        return setTypeId(type, true);
    }

    public boolean setTypeId(final int type, final boolean applyPhysics) {
        net.minecraft.server.Block block = getNMSBlock(type);
        return setTypeIdAndData(type, (byte) block.toLegacyData(block.getBlockData()), applyPhysics);
    }

    public boolean setTypeIdAndData(final int type, final byte data, final boolean applyPhysics) {
        IBlockData blockData = getNMSBlock(type).fromLegacyData(data);
        BlockPosition position = new BlockPosition(x, y, z);
        if (applyPhysics) {
            return chunk.getHandle().getWorld().setTypeAndData(position, blockData, 3);
        } else {
            boolean success = chunk.getHandle().getWorld().setTypeAndData(position, blockData, 2);
            if (success) {
                chunk.getHandle().getWorld().notify(position);
            }
            return success;
        }
    }

    public Material getType() {
        return Material.getMaterial(getTypeId());
    }

    @Deprecated
    @Override
    public int getTypeId() {
        return CraftMagicNumbers.getId(chunk.getHandle().getType(new BlockPosition(this.x, this.y, this.z)));
    }

    public byte getLightLevel() {
        return (byte) chunk.getHandle().getWorld().getLightLevel(new BlockPosition(this.x, this.y, this.z));
    }

    public byte getLightFromSky() {
        return (byte) chunk.getHandle().getBrightness(EnumSkyBlock.SKY, new BlockPosition(this.x, this.y, this.z));
    }

    public byte getLightFromBlocks() {
        return (byte) chunk.getHandle().getBrightness(EnumSkyBlock.BLOCK, new BlockPosition(this.x, this.y, this.z));
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

    public static BlockFace notchToBlockFace(EnumDirection notch) {
        if (notch == null) return BlockFace.SELF;
        switch (notch) {
        case DOWN:
            return BlockFace.DOWN;
        case UP:
            return BlockFace.UP;
        case NORTH:
            return BlockFace.NORTH;
        case SOUTH:
            return BlockFace.SOUTH;
        case WEST:
            return BlockFace.WEST;
        case EAST:
            return BlockFace.EAST;
        default:
            return BlockFace.SELF;
        }
    }

    public static EnumDirection blockFaceToNotch(BlockFace face) {
        switch (face) {
        case DOWN:
            return EnumDirection.DOWN;
        case UP:
            return EnumDirection.UP;
        case NORTH:
            return EnumDirection.NORTH;
        case SOUTH:
            return EnumDirection.SOUTH;
        case WEST:
            return EnumDirection.WEST;
        case EAST:
            return EnumDirection.EAST;
        default:
            return null;
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
        case TRAPPED_CHEST:
            return new CraftChest(this);
        case BURNING_FURNACE:
        case FURNACE:
            return new CraftFurnace(this);
        case DISPENSER:
            return new CraftDispenser(this);
        case DROPPER:
            return new CraftDropper(this);
        case HOPPER:
            return new CraftHopper(this);
        case MOB_SPAWNER:
            return new CraftCreatureSpawner(this);
        case NOTE_BLOCK:
            return new CraftNoteBlock(this);
        case JUKEBOX:
            return new CraftJukebox(this);
        case BREWING_STAND:
            return new CraftBrewingStand(this);
        case SKULL:
            return new CraftSkull(this);
        case COMMAND:
            return new CraftCommandBlock(this);
        case BEACON:
            return new CraftBeacon(this);
        case BANNER:
        case WALL_BANNER:
        case STANDING_BANNER:
            return new CraftBanner(this);
        default:
            return new CraftBlockState(this);
        }
    }

    public Biome getBiome() {
        return getWorld().getBiome(x, z);
    }

    public void setBiome(Biome bio) {
        getWorld().setBiome(x, z, bio);
    }

    public static Biome biomeBaseToBiome(BiomeBase base) {
        if (base == null) {
            return null;
        }

        return BIOME_MAPPING[base.id];
    }

    public static BiomeBase biomeToBiomeBase(Biome bio) {
        if (bio == null) {
            return null;
        }
        return BIOMEBASE_MAPPING[bio.ordinal()];
    }

    public double getTemperature() {
        return getWorld().getTemperature(x, z);
    }

    public double getHumidity() {
        return getWorld().getHumidity(x, z);
    }

    public boolean isBlockPowered() {
        return chunk.getHandle().getWorld().getBlockPower(new BlockPosition(x, y, z)) > 0;
    }

    public boolean isBlockIndirectlyPowered() {
        return chunk.getHandle().getWorld().isBlockIndirectlyPowered(new BlockPosition(x, y, z));
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
        return chunk.getHandle().getWorld().isBlockFacePowered(new BlockPosition(x, y, z), blockFaceToNotch(face));
    }

    public boolean isBlockFaceIndirectlyPowered(BlockFace face) {
        int power = chunk.getHandle().getWorld().getBlockFacePower(new BlockPosition(x, y, z), blockFaceToNotch(face));

        Block relative = getRelative(face);
        if (relative.getType() == Material.REDSTONE_WIRE) {
            return Math.max(power, relative.getData()) > 0;
        }

        return power > 0;
    }

    public int getBlockPower(BlockFace face) {
        int power = 0;
        BlockRedstoneWire wire = Blocks.REDSTONE_WIRE;
        net.minecraft.server.World world = chunk.getHandle().getWorld();
        if ((face == BlockFace.DOWN || face == BlockFace.SELF) && world.isBlockFacePowered(new BlockPosition(x, y - 1, z), EnumDirection.DOWN)) power = wire.getPower(world, new BlockPosition(x, y - 1, z), power);
        if ((face == BlockFace.UP || face == BlockFace.SELF) && world.isBlockFacePowered(new BlockPosition(x, y + 1, z), EnumDirection.UP)) power = wire.getPower(world, new BlockPosition(x, y + 1, z), power);
        if ((face == BlockFace.EAST || face == BlockFace.SELF) && world.isBlockFacePowered(new BlockPosition(x + 1, y, z), EnumDirection.EAST)) power = wire.getPower(world, new BlockPosition(x + 1, y, z), power);
        if ((face == BlockFace.WEST || face == BlockFace.SELF) && world.isBlockFacePowered(new BlockPosition(x - 1, y, z), EnumDirection.WEST)) power = wire.getPower(world, new BlockPosition(x - 1, y, z), power);
        if ((face == BlockFace.NORTH || face == BlockFace.SELF) && world.isBlockFacePowered(new BlockPosition(x, y, z - 1), EnumDirection.NORTH)) power = wire.getPower(world, new BlockPosition(x, y, z - 1), power);
        if ((face == BlockFace.SOUTH || face == BlockFace.SELF) && world.isBlockFacePowered(new BlockPosition(x, y, z + 1), EnumDirection.SOUTH)) power = wire.getPower(world, new BlockPosition(x, y, z - 1), power);
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
        return PistonMoveReaction.getById(getNMSBlock().getMaterial().getPushReaction());
    }

    private boolean itemCausesDrops(ItemStack item) {
        net.minecraft.server.Block block = this.getNMSBlock();
        net.minecraft.server.Item itemType = item != null ? net.minecraft.server.Item.getById(item.getTypeId()) : null;
        return block != null && (block.getMaterial().isAlwaysDestroyable() || (itemType != null && itemType.canDestroySpecialBlock(block)));
    }

    public boolean breakNaturally() {
        // Order matters here, need to drop before setting to air so skulls can get their data
        net.minecraft.server.Block block = this.getNMSBlock();
        byte data = getData();
        boolean result = false;

        if (block != null && block != Blocks.AIR) {
            block.dropNaturally(chunk.getHandle().getWorld(), new BlockPosition(x, y, z), block.fromLegacyData(data), 1.0F, 0);
            result = true;
        }

        setTypeId(Material.AIR.getId());
        return result;
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

        net.minecraft.server.Block block = this.getNMSBlock();
        if (block != Blocks.AIR) {
            byte data = getData();
            // based on nms.Block.dropNaturally
            int count = block.getDropCount(0, chunk.getHandle().getWorld().random);
            for (int i = 0; i < count; ++i) {
                Item item = block.getDropType(block.fromLegacyData(data), chunk.getHandle().getWorld().random, 0);
                if (item != null) {
                    // Skulls are special, their data is based on the tile entity
                    if (Blocks.SKULL == block) {
                        net.minecraft.server.ItemStack nmsStack = new net.minecraft.server.ItemStack(item, 1, block.getDropData(chunk.getHandle().getWorld(), new BlockPosition(x, y, z)));
                        TileEntitySkull tileentityskull = (TileEntitySkull) chunk.getHandle().getWorld().getTileEntity(new BlockPosition(x, y, z));

                        if (tileentityskull.getSkullType() == 3 && tileentityskull.getGameProfile() != null) {
                            nmsStack.setTag(new NBTTagCompound());
                            NBTTagCompound nbttagcompound = new NBTTagCompound();

                            GameProfileSerializer.serialize(nbttagcompound, tileentityskull.getGameProfile());
                            nmsStack.getTag().set("SkullOwner", nbttagcompound);
                        }

                        drops.add(CraftItemStack.asBukkitCopy(nmsStack));
                        // We don't want to drop cocoa blocks, we want to drop cocoa beans.
                    } else if (Blocks.COCOA == block) {
                        int age = (Integer) block.fromLegacyData(data).get(BlockCocoa.AGE);
                        int dropAmount = (age >= 2 ? 3 : 1);
                        for (int j = 0; j < dropAmount; ++j) {
                            drops.add(new ItemStack(Material.INK_SACK, 1, (short) 3));
                        }
                    } else {
                        drops.add(new ItemStack(org.bukkit.craftbukkit.util.CraftMagicNumbers.getMaterial(item), 1, (short) block.getDropData(block.fromLegacyData(data))));
                    }
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

    /* Build biome index based lookup table for BiomeBase to Biome mapping */
    static {
        BIOME_MAPPING = new Biome[BiomeBase.getBiomes().length];
        BIOMEBASE_MAPPING = new BiomeBase[Biome.values().length];
        BIOME_MAPPING[BiomeBase.OCEAN.id] = Biome.OCEAN;
        BIOME_MAPPING[BiomeBase.PLAINS.id] = Biome.PLAINS;
        BIOME_MAPPING[BiomeBase.DESERT.id] = Biome.DESERT;
        BIOME_MAPPING[BiomeBase.EXTREME_HILLS.id] = Biome.EXTREME_HILLS;
        BIOME_MAPPING[BiomeBase.FOREST.id] = Biome.FOREST;
        BIOME_MAPPING[BiomeBase.TAIGA.id] = Biome.TAIGA;
        BIOME_MAPPING[BiomeBase.SWAMPLAND.id] = Biome.SWAMPLAND;
        BIOME_MAPPING[BiomeBase.RIVER.id] = Biome.RIVER;
        BIOME_MAPPING[BiomeBase.HELL.id] = Biome.HELL;
        BIOME_MAPPING[BiomeBase.SKY.id] = Biome.SKY;
        BIOME_MAPPING[BiomeBase.FROZEN_OCEAN.id] = Biome.FROZEN_OCEAN;
        BIOME_MAPPING[BiomeBase.FROZEN_RIVER.id] = Biome.FROZEN_RIVER;
        BIOME_MAPPING[BiomeBase.ICE_PLAINS.id] = Biome.ICE_PLAINS;
        BIOME_MAPPING[BiomeBase.ICE_MOUNTAINS.id] = Biome.ICE_MOUNTAINS;
        BIOME_MAPPING[BiomeBase.MUSHROOM_ISLAND.id] = Biome.MUSHROOM_ISLAND;
        BIOME_MAPPING[BiomeBase.MUSHROOM_SHORE.id] = Biome.MUSHROOM_SHORE;
        BIOME_MAPPING[BiomeBase.BEACH.id] = Biome.BEACH;
        BIOME_MAPPING[BiomeBase.DESERT_HILLS.id] = Biome.DESERT_HILLS;
        BIOME_MAPPING[BiomeBase.FOREST_HILLS.id] = Biome.FOREST_HILLS;
        BIOME_MAPPING[BiomeBase.TAIGA_HILLS.id] = Biome.TAIGA_HILLS;
        BIOME_MAPPING[BiomeBase.SMALL_MOUNTAINS.id] = Biome.SMALL_MOUNTAINS;
        BIOME_MAPPING[BiomeBase.JUNGLE.id] = Biome.JUNGLE;
        BIOME_MAPPING[BiomeBase.JUNGLE_HILLS.id] = Biome.JUNGLE_HILLS;
        BIOME_MAPPING[BiomeBase.JUNGLE_EDGE.id] = Biome.JUNGLE_EDGE;
        BIOME_MAPPING[BiomeBase.DEEP_OCEAN.id] = Biome.DEEP_OCEAN;
        BIOME_MAPPING[BiomeBase.STONE_BEACH.id] = Biome.STONE_BEACH;
        BIOME_MAPPING[BiomeBase.COLD_BEACH.id] = Biome.COLD_BEACH;
        BIOME_MAPPING[BiomeBase.BIRCH_FOREST.id] = Biome.BIRCH_FOREST;
        BIOME_MAPPING[BiomeBase.BIRCH_FOREST_HILLS.id] = Biome.BIRCH_FOREST_HILLS;
        BIOME_MAPPING[BiomeBase.ROOFED_FOREST.id] = Biome.ROOFED_FOREST;
        BIOME_MAPPING[BiomeBase.COLD_TAIGA.id] = Biome.COLD_TAIGA;
        BIOME_MAPPING[BiomeBase.COLD_TAIGA_HILLS.id] = Biome.COLD_TAIGA_HILLS;
        BIOME_MAPPING[BiomeBase.MEGA_TAIGA.id] = Biome.MEGA_TAIGA;
        BIOME_MAPPING[BiomeBase.MEGA_TAIGA_HILLS.id] = Biome.MEGA_TAIGA_HILLS;
        BIOME_MAPPING[BiomeBase.EXTREME_HILLS_PLUS.id] = Biome.EXTREME_HILLS_PLUS;
        BIOME_MAPPING[BiomeBase.SAVANNA.id] = Biome.SAVANNA;
        BIOME_MAPPING[BiomeBase.SAVANNA_PLATEAU.id] = Biome.SAVANNA_PLATEAU;
        BIOME_MAPPING[BiomeBase.MESA.id] = Biome.MESA;
        BIOME_MAPPING[BiomeBase.MESA_PLATEAU_F.id] = Biome.MESA_PLATEAU_FOREST;
        BIOME_MAPPING[BiomeBase.MESA_PLATEAU.id] = Biome.MESA_PLATEAU;

        // Extended Biomes
        BIOME_MAPPING[BiomeBase.PLAINS.id + 128] = Biome.SUNFLOWER_PLAINS;
        BIOME_MAPPING[BiomeBase.DESERT.id + 128] = Biome.DESERT_MOUNTAINS;
        BIOME_MAPPING[BiomeBase.FOREST.id + 128] = Biome.FLOWER_FOREST;
        BIOME_MAPPING[BiomeBase.TAIGA.id + 128] = Biome.TAIGA_MOUNTAINS;
        BIOME_MAPPING[BiomeBase.SWAMPLAND.id + 128] = Biome.SWAMPLAND_MOUNTAINS;
        BIOME_MAPPING[BiomeBase.ICE_PLAINS.id + 128] = Biome.ICE_PLAINS_SPIKES;
        BIOME_MAPPING[BiomeBase.JUNGLE.id + 128] = Biome.JUNGLE_MOUNTAINS;
        BIOME_MAPPING[BiomeBase.JUNGLE_EDGE.id + 128] = Biome.JUNGLE_EDGE_MOUNTAINS;
        BIOME_MAPPING[BiomeBase.COLD_TAIGA.id + 128] = Biome.COLD_TAIGA_MOUNTAINS;
        BIOME_MAPPING[BiomeBase.SAVANNA.id + 128] = Biome.SAVANNA_MOUNTAINS;
        BIOME_MAPPING[BiomeBase.SAVANNA_PLATEAU.id + 128] = Biome.SAVANNA_PLATEAU_MOUNTAINS;
        BIOME_MAPPING[BiomeBase.MESA.id + 128] = Biome.MESA_BRYCE;
        BIOME_MAPPING[BiomeBase.MESA_PLATEAU_F.id + 128] = Biome.MESA_PLATEAU_FOREST_MOUNTAINS;
        BIOME_MAPPING[BiomeBase.MESA_PLATEAU.id + 128] = Biome.MESA_PLATEAU_MOUNTAINS;
        BIOME_MAPPING[BiomeBase.BIRCH_FOREST.id + 128] = Biome.BIRCH_FOREST_MOUNTAINS;
        BIOME_MAPPING[BiomeBase.BIRCH_FOREST_HILLS.id + 128] = Biome.BIRCH_FOREST_HILLS_MOUNTAINS;
        BIOME_MAPPING[BiomeBase.ROOFED_FOREST.id + 128] = Biome.ROOFED_FOREST_MOUNTAINS;
        BIOME_MAPPING[BiomeBase.MEGA_TAIGA.id + 128] = Biome.MEGA_SPRUCE_TAIGA;
        BIOME_MAPPING[BiomeBase.EXTREME_HILLS.id + 128] = Biome.EXTREME_HILLS_MOUNTAINS;
        BIOME_MAPPING[BiomeBase.EXTREME_HILLS_PLUS.id + 128] = Biome.EXTREME_HILLS_PLUS_MOUNTAINS;
        BIOME_MAPPING[BiomeBase.MEGA_TAIGA_HILLS.id + 128] = Biome.MEGA_SPRUCE_TAIGA_HILLS;

        /* Sanity check - we should have a record for each record in the BiomeBase.a table */
        /* Helps avoid missed biomes when we upgrade bukkit to new code with new biomes */
        for (int i = 0; i < BIOME_MAPPING.length; i++) {
            if ((BiomeBase.getBiome(i) != null) && (BIOME_MAPPING[i] == null)) {
                throw new IllegalArgumentException("Missing Biome mapping for BiomeBase[" + i + ", " + BiomeBase.getBiome(i) + "]");
            }
            if (BIOME_MAPPING[i] != null) {  /* Build reverse mapping for setBiome */
                BIOMEBASE_MAPPING[BIOME_MAPPING[i].ordinal()] = BiomeBase.getBiome(i);
            }
        }
    }

    public void setMetadata(String metadataKey, MetadataValue newMetadataValue) {
        chunk.getCraftWorld().getBlockMetadata().setMetadata(this, metadataKey, newMetadataValue);
    }

    public List<MetadataValue> getMetadata(String metadataKey) {
        return chunk.getCraftWorld().getBlockMetadata().getMetadata(this, metadataKey);
    }

    public boolean hasMetadata(String metadataKey) {
        return chunk.getCraftWorld().getBlockMetadata().hasMetadata(this, metadataKey);
    }

    public void removeMetadata(String metadataKey, Plugin owningPlugin) {
        chunk.getCraftWorld().getBlockMetadata().removeMetadata(this, metadataKey, owningPlugin);
    }
}
