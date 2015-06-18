package org.bukkit.craftbukkit.block;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.TileEntitySkull;
import org.bukkit.Material;

import org.bukkit.SkullType;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Skull;
import org.bukkit.craftbukkit.CraftWorld;

public class CraftSkull extends CraftBlockState implements Skull {
    private static final int MAX_OWNER_LENGTH = 16;
    private final TileEntitySkull skull;
    private GameProfile profile;
    private SkullType skullType;
    private byte rotation;

    public CraftSkull(final Block block) {
        super(block);

        CraftWorld world = (CraftWorld) block.getWorld();
        skull = (TileEntitySkull) world.getTileEntityAt(getX(), getY(), getZ());
        profile = skull.getGameProfile();
        skullType = getSkullType(skull.getSkullType());
        rotation = (byte) skull.getRotation();
    }

    public CraftSkull(final Material material, final TileEntitySkull te) {
        super(material);
        skull = te;
        profile = skull.getGameProfile();
        skullType = getSkullType(skull.getSkullType());
        rotation = (byte) skull.getRotation();
    }

    static SkullType getSkullType(int id) {
        switch (id) {
            default:
            case 0:
                return SkullType.SKELETON;
            case 1:
                return SkullType.WITHER;
            case 2:
                return SkullType.ZOMBIE;
            case 3:
                return SkullType.PLAYER;
            case 4:
                return SkullType.CREEPER;
        }
    }

    static int getSkullType(SkullType type) {
        switch(type) {
            default:
            case SKELETON:
                return 0;
            case WITHER:
                return 1;
            case ZOMBIE:
                return 2;
            case PLAYER:
                return 3;
            case CREEPER:
                return 4;
        }
    }

    static byte getBlockFace(BlockFace rotation) {
        switch (rotation) {
            case NORTH:
                return 0;
            case NORTH_NORTH_EAST:
                return 1;
            case NORTH_EAST:
                return 2;
            case EAST_NORTH_EAST:
                return 3;
            case EAST:
                return 4;
            case EAST_SOUTH_EAST:
                return 5;
            case SOUTH_EAST:
                return 6;
            case SOUTH_SOUTH_EAST:
                return 7;
            case SOUTH:
                return 8;
            case SOUTH_SOUTH_WEST:
                return 9;
            case SOUTH_WEST:
                return 10;
            case WEST_SOUTH_WEST:
                return 11;
            case WEST:
                return 12;
            case WEST_NORTH_WEST:
                return 13;
            case NORTH_WEST:
                return 14;
            case NORTH_NORTH_WEST:
                return 15;
            default:
                throw new IllegalArgumentException("Invalid BlockFace rotation: " + rotation);
        }
    }

    static BlockFace getBlockFace(byte rotation) {
        switch (rotation) {
            case 0:
                return BlockFace.NORTH;
            case 1:
                return BlockFace.NORTH_NORTH_EAST;
            case 2:
                return BlockFace.NORTH_EAST;
            case 3:
                return BlockFace.EAST_NORTH_EAST;
            case 4:
                return BlockFace.EAST;
            case 5:
                return BlockFace.EAST_SOUTH_EAST;
            case 6:
                return BlockFace.SOUTH_EAST;
            case 7:
                return BlockFace.SOUTH_SOUTH_EAST;
            case 8:
                return BlockFace.SOUTH;
            case 9:
                return BlockFace.SOUTH_SOUTH_WEST;
            case 10:
                return BlockFace.SOUTH_WEST;
            case 11:
                return BlockFace.WEST_SOUTH_WEST;
            case 12:
                return BlockFace.WEST;
            case 13:
                return BlockFace.WEST_NORTH_WEST;
            case 14:
                return BlockFace.NORTH_WEST;
            case 15:
                return BlockFace.NORTH_NORTH_WEST;
            default:
                throw new AssertionError(rotation);
        }
    }

    public boolean hasOwner() {
        return profile != null;
    }

    public String getOwner() {
        return hasOwner() ? profile.getName() : null;
    }

    public boolean setOwner(String name) {
        if (name == null || name.length() > MAX_OWNER_LENGTH) {
            return false;
        }

        GameProfile profile = MinecraftServer.getServer().getUserCache().getProfile(name);
        if (profile == null) {
            return false;
        }

        if (skullType != SkullType.PLAYER) {
            skullType = SkullType.PLAYER;
        }

        this.profile = profile;
        return true;
    }

    public BlockFace getRotation() {
    	return getBlockFace(rotation);
    }

    public void setRotation(BlockFace rotation) {
        this.rotation = getBlockFace(rotation);
    }

    public SkullType getSkullType() {
        return skullType;
    }

    public void setSkullType(SkullType skullType) {
        this.skullType = skullType;

        if (skullType != SkullType.PLAYER) {
            profile = null;
        }
    }

    @Override
    public boolean update(boolean force, boolean applyPhysics) {
        boolean result = super.update(force, applyPhysics);

        if (result) {
            if (skullType == SkullType.PLAYER) {
                skull.setGameProfile(profile);
            } else {
                skull.setSkullType(getSkullType(skullType));
            }

            skull.setRotation(rotation);
            skull.update();
        }

        return result;
    }

    @Override
    public TileEntitySkull getTileEntity() {
        return skull;
    }
}
