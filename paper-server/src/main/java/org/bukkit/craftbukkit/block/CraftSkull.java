package org.bukkit.craftbukkit.block;

import com.destroystokyo.paper.profile.SharedPlayerProfile;
import com.google.common.base.Preconditions;
import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.util.Either;
import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.datacomponent.item.PaperResolvableProfile;
import net.kyori.adventure.text.Component;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.players.NameAndId;
import net.minecraft.world.entity.player.PlayerSkin;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
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
import java.util.Objects;

public class CraftSkull extends CraftBlockEntityState<SkullBlockEntity> implements Skull {

    private static final int MAX_OWNER_LENGTH = 16;
    private ResolvableProfile profile;

    public CraftSkull(World world, SkullBlockEntity blockEntity) {
        super(world, blockEntity);
    }

    protected CraftSkull(CraftSkull state, Location location) {
        super(state, location);
    }

    @Override
    public void load(SkullBlockEntity blockEntity) {
        super.load(blockEntity);

        ResolvableProfile owner = blockEntity.getOwnerProfile();
        if (owner != null) {
            this.profile = owner;
        }
    }

    @Override
    public io.papermc.paper.datacomponent.item.ResolvableProfile getProfile() {
        if (this.profile == null) {
            return null;
        }
        return new PaperResolvableProfile(this.profile);
    }

    @Override
    public void setProfile(final io.papermc.paper.datacomponent.item.ResolvableProfile profile) {
        if (profile == null) {
            this.profile = null;
            return;
        }
        this.profile = ((PaperResolvableProfile) profile).getHandle();
    }

    @Override
    public boolean hasOwner() {
        return this.profile != null;
    }

    @Override
    public String getOwner() {
        return this.hasOwner() ? this.profile.name().orElse(null) : null;
    }

    @Override
    public boolean setOwner(String name) {
        if (name == null || name.length() > CraftSkull.MAX_OWNER_LENGTH) {
            return false;
        }

        GameProfile profile = MinecraftServer.getServer().services().paper().filledProfileCache().getIfCached(name);
        if (profile == null) {
            profile = MinecraftServer.getServer().services().nameToIdCache().get(name).map(NameAndId::toUncompletedGameProfile).orElse(null);
        }
        if (profile == null) {
            return false;
        }

        this.profile = ResolvableProfile.createResolved(profile);
        return true;
    }

    @Override
    public OfflinePlayer getOwningPlayer() {
        if (this.hasOwner()) {
            final GameProfile gameProfile = this.profile.partialProfile(); // The partial profile is always guaranteed to have a non-null uuid and name.
            if (Objects.equals(gameProfile.id(), Util.NIL_UUID)) {
                return Bukkit.getOfflinePlayer(gameProfile.id());
            }

            if (!gameProfile.name().isEmpty()) {
                return Bukkit.getOfflinePlayer(gameProfile.name());
            }
        }

        return null;
    }

    @Override
    public void setOwningPlayer(OfflinePlayer player) {
        Preconditions.checkNotNull(player, "player");

        if (player instanceof CraftPlayer craftPlayer) {
            this.profile = ResolvableProfile.createResolved(craftPlayer.getProfile());
        } else {
            this.profile = new ResolvableProfile.Dynamic(Either.right(player.getUniqueId()), PlayerSkin.Patch.EMPTY);
        }
    }

    // Paper start
    @Override
    public void setPlayerProfile(com.destroystokyo.paper.profile.PlayerProfile profile) {
        Preconditions.checkNotNull(profile, "profile");
        this.profile = com.destroystokyo.paper.profile.CraftPlayerProfile.asResolvableProfileCopy(profile);
    }

    @javax.annotation.Nullable
    @Override
    public com.destroystokyo.paper.profile.PlayerProfile getPlayerProfile() {
        return profile != null ? new com.destroystokyo.paper.profile.CraftPlayerProfile(profile) : null;
    }
    // Paper end

    @Override
    @Deprecated // Paper
    public PlayerProfile getOwnerProfile() {
        if (!this.hasOwner()) {
            return null;
        }

        return new CraftPlayerProfile(this.profile);
    }

    @Override
    @Deprecated // Paper
    public void setOwnerProfile(PlayerProfile profile) {
        if (profile == null) {
            this.profile = null;
        } else {
            this.profile = ((SharedPlayerProfile) profile).buildResolvableProfile(); // Paper
        }
    }

    @Override
    public NamespacedKey getNoteBlockSound() {
        ResourceLocation key = this.getSnapshot().getNoteBlockSound();
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
        BlockData blockData = this.getBlockData();
        return (blockData instanceof Rotatable rotatable) ? rotatable.getRotation() : ((Directional) blockData).getFacing();
    }

    @Override
    public void setRotation(BlockFace rotation) {
        BlockData blockData = this.getBlockData();
        if (blockData instanceof Rotatable) {
            ((Rotatable) blockData).setRotation(rotation);
        } else {
            ((Directional) blockData).setFacing(rotation);
        }
        this.setBlockData(blockData);
    }

    @Override
    public SkullType getSkullType() {
        switch (this.getType()) {
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
                throw new IllegalArgumentException("Unknown SkullType for " + this.getType());
        }
    }

    @Override
    public void setSkullType(SkullType skullType) {
        throw new UnsupportedOperationException("Must change block type");
    }

    @Override
    public void applyTo(SkullBlockEntity blockEntity) {
        super.applyTo(blockEntity);

        if (this.getSkullType() == SkullType.PLAYER) {
            blockEntity.owner = (this.hasOwner() ? this.profile : null);
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

    @Override
    public @Nullable Component customName() {
        SkullBlockEntity snapshot = getSnapshot();
        return snapshot.customName == null ? null : PaperAdventure.asAdventure(snapshot.customName);
    }

    @Override
    public void customName(@Nullable Component customName) {
        SkullBlockEntity snapshot = getSnapshot();
        snapshot.customName = customName == null ? null : PaperAdventure.asVanilla(customName);
    }
}
