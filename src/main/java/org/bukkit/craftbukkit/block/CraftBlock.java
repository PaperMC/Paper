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

    private IBlockData getData0() {
        return chunk.getHandle().getBlockData(new BlockPosition(x, y, z));
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

        // SPIGOT-611: need to do this to prevent glitchiness. Easier to handle this here (like /setblock) than to fix weirdness in tile entity cleanup
        chunk.getHandle().getWorld().setTypeAndData(position, Blocks.AIR.getBlockData(), 0);

        if (applyPhysics) {
            return chunk.getHandle().getWorld().setTypeAndData(position, blockData, 3);
        } else {
            IBlockData old = chunk.getHandle().getBlockData(position);
            boolean success = chunk.getHandle().getWorld().setTypeAndData(position, blockData, 2);
            if (success) {
                chunk.getHandle().getWorld().notify(
                        position,
                        old,
                        blockData,
                        3
                );
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
        return CraftMagicNumbers.getId(chunk.getHandle().getBlockData(new BlockPosition(this.x, this.y, this.z)).getBlock());
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
        case END_GATEWAY:
            return new CraftEndGateway(this);
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
        case COMMAND_CHAIN:
        case COMMAND_REPEATING:
            return new CraftCommandBlock(this);
        case BEACON:
            return new CraftBeacon(this);
        case BANNER:
        case WALL_BANNER:
        case STANDING_BANNER:
            return new CraftBanner(this);
        case FLOWER_POT:
            return new CraftFlowerPot(this);
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

        return Biome.valueOf(BiomeBase.REGISTRY_ID.b(base).a().toUpperCase());
    }

    public static BiomeBase biomeToBiomeBase(Biome bio) {
        if (bio == null) {
            return null;
        }

        return BiomeBase.REGISTRY_ID.get(new MinecraftKey(bio.name().toLowerCase()));
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
        return PistonMoveReaction.getById(getNMSBlock().getBlockData().getMaterial().getPushReaction().ordinal());
    }

    private boolean itemCausesDrops(ItemStack item) {
        net.minecraft.server.Block block = this.getNMSBlock();
        net.minecraft.server.Item itemType = item != null ? net.minecraft.server.Item.getById(item.getTypeId()) : null;
        return block != null && (block.getBlockData().getMaterial().isAlwaysDestroyable() || (itemType != null && itemType.canDestroySpecialBlock(block.getBlockData())));
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
            IBlockData data = getData0();
            // based on nms.Block.dropNaturally
            int count = block.getDropCount(0, chunk.getHandle().getWorld().random);
            for (int i = 0; i < count; ++i) {
                Item item = block.getDropType(data, chunk.getHandle().getWorld().random, 0);
                if (item != null) {
                    // Skulls are special, their data is based on the tile entity
                    if (Blocks.SKULL == block) {
                        net.minecraft.server.ItemStack nmsStack = new net.minecraft.server.ItemStack(item, 1, block.getDropData(data));
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
                        int age = (Integer) data.get(BlockCocoa.AGE);
                        int dropAmount = (age >= 2 ? 3 : 1);
                        for (int j = 0; j < dropAmount; ++j) {
                            drops.add(new ItemStack(Material.INK_SACK, 1, (short) 3));
                        }
                    } else {
                        drops.add(new ItemStack(org.bukkit.craftbukkit.util.CraftMagicNumbers.getMaterial(item), 1, (short) block.getDropData(data)));
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
