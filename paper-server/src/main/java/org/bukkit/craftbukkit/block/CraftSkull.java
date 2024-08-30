package org.bukkit.craftbukkit.block;

import com.google.common.base.Preconditions;
import com.mojang.authlib.GameProfile;
import net.minecraft.SystemUtils;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.block.entity.TileEntitySkull;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.SkullType;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Skull;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Rotatable;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.profile.CraftPlayerProfile;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.profile.PlayerProfile;
import org.jetbrains.annotations.Nullable;

public class CraftSkull extends CraftBlockEntityState<TileEntitySkull> implements Skull {

    private static final int MAX_OWNER_LENGTH = 16;
    private ResolvableProfile profile;

    public CraftSkull(World world, TileEntitySkull tileEntity) {
        super(world, tileEntity);
    }

    protected CraftSkull(CraftSkull state, Location location) {
        super(state, location);
    }

    @Override
    public void load(TileEntitySkull skull) {
        super.load(skull);

        ResolvableProfile owner = skull.getOwnerProfile();
        if (owner != null) {
            profile = owner;
        }
    }

    @Override
    public boolean hasOwner() {
        return profile != null;
    }

    @Override
    public String getOwner() {
        return hasOwner() ? profile.name().orElse(null) : null;
    }

    @Override
    public boolean setOwner(String name) {
        if (name == null || name.length() > MAX_OWNER_LENGTH) {
            return false;
        }

        GameProfile profile = MinecraftServer.getServer().getProfileCache().get(name).orElse(null);
        if (profile == null) {
            return false;
        }

        this.profile = new ResolvableProfile(profile);
        return true;
    }

    @Override
    public OfflinePlayer getOwningPlayer() {
        if (hasOwner()) {
            if (profile.id().filter(u -> !u.equals(SystemUtils.NIL_UUID)).isPresent()) {
                return Bukkit.getOfflinePlayer(profile.id().get());
            }

            if (profile.name().filter(s -> !s.isEmpty()).isPresent()) {
                return Bukkit.getOfflinePlayer(profile.name().get());
            }
        }

        return null;
    }

    @Override
    public void setOwningPlayer(OfflinePlayer player) {
        Preconditions.checkNotNull(player, "player");

        if (player instanceof CraftPlayer craftPlayer) {
            this.profile = new ResolvableProfile(craftPlayer.getProfile());
        } else {
            this.profile = new ResolvableProfile(new GameProfile(player.getUniqueId(), (player.getName() == null) ? "" : player.getName()));
        }
    }

    @Override
    public PlayerProfile getOwnerProfile() {
        if (!hasOwner()) {
            return null;
        }

        return new CraftPlayerProfile(profile);
    }

    @Override
    public void setOwnerProfile(PlayerProfile profile) {
        if (profile == null) {
            this.profile = null;
        } else {
            this.profile = new ResolvableProfile(CraftPlayerProfile.validateSkullProfile(((CraftPlayerProfile) profile).buildGameProfile()));
        }
    }

    @Override
    public NamespacedKey getNoteBlockSound() {
        MinecraftKey key = getSnapshot().getNoteBlockSound();
        return (key != null) ? CraftNamespacedKey.fromMinecraft(key) : null;
    }

    @Override
    public void setNoteBlockSound(@Nullable NamespacedKey namespacedKey) {
        if (namespacedKey == null) {
            this.getSnapshot().noteBlockSound = null;
            return;
        }
        this.getSnapshot().noteBlockSound = CraftNamespacedKey.toMinecraft(namespacedKey);
    }

    @Override
    public BlockFace getRotation() {
        BlockData blockData = getBlockData();
        return (blockData instanceof Rotatable rotatable) ? rotatable.getRotation() : ((Directional) blockData).getFacing();
    }

    @Override
    public void setRotation(BlockFace rotation) {
        BlockData blockData = getBlockData();
        if (blockData instanceof Rotatable) {
            ((Rotatable) blockData).setRotation(rotation);
        } else {
            ((Directional) blockData).setFacing(rotation);
        }
        setBlockData(blockData);
    }

    @Override
    public SkullType getSkullType() {
        switch (getType()) {
            case SKELETON_SKULL:
            case SKELETON_WALL_SKULL:
                return SkullType.SKELETON;
            case WITHER_SKELETON_SKULL:
            case WITHER_SKELETON_WALL_SKULL:
                return SkullType.WITHER;
            case ZOMBIE_HEAD:
            case ZOMBIE_WALL_HEAD:
                return SkullType.ZOMBIE;
            case PIGLIN_HEAD:
            case PIGLIN_WALL_HEAD:
                return SkullType.PIGLIN;
            case PLAYER_HEAD:
            case PLAYER_WALL_HEAD:
                return SkullType.PLAYER;
            case CREEPER_HEAD:
            case CREEPER_WALL_HEAD:
                return SkullType.CREEPER;
            case DRAGON_HEAD:
            case DRAGON_WALL_HEAD:
                return SkullType.DRAGON;
            default:
                throw new IllegalArgumentException("Unknown SkullType for " + getType());
        }
    }

    @Override
    public void setSkullType(SkullType skullType) {
        throw new UnsupportedOperationException("Must change block type");
    }

    @Override
    public void applyTo(TileEntitySkull skull) {
        super.applyTo(skull);

        if (getSkullType() == SkullType.PLAYER) {
            skull.setOwner(hasOwner() ? profile : null);
        }
    }

    @Override
    public CraftSkull copy() {
        return new CraftSkull(this, null);
    }

    @Override
    public CraftSkull copy(Location location) {
        return new CraftSkull(this, location);
    }
}
