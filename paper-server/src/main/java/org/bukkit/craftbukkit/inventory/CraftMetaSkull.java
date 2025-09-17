package org.bukkit.craftbukkit.inventory;

import com.google.common.collect.ImmutableMap.Builder;
import com.mojang.authlib.GameProfile;
import java.util.Map;
import java.util.Objects;
import com.mojang.datafixers.util.Either;
import net.minecraft.Util;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.PlayerSkin;
import net.minecraft.world.item.component.ResolvableProfile;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.profile.CraftPlayerProfile;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.jetbrains.annotations.Nullable;

@DelegateDeserialization(SerializableMeta.class)
class CraftMetaSkull extends CraftMetaItem implements SkullMeta {

    @ItemMetaKey.Specific(ItemMetaKey.Specific.To.NBT)
    static final ItemMetaKeyType<ResolvableProfile> SKULL_PROFILE = new ItemMetaKeyType<>(DataComponents.PROFILE, "SkullProfile");

    static final ItemMetaKey SKULL_OWNER = new ItemMetaKey("skull-owner");

    @ItemMetaKey.Specific(ItemMetaKey.Specific.To.NBT)
    static final ItemMetaKey BLOCK_ENTITY_TAG = new ItemMetaKey("BlockEntityTag");
    static final ItemMetaKeyType<ResourceLocation> NOTE_BLOCK_SOUND = new ItemMetaKeyType<>(DataComponents.NOTE_BLOCK_SOUND, "note_block_sound");
    static final int MAX_OWNER_LENGTH = 16;

    private ResolvableProfile profile;
    private ResourceLocation noteBlockSound;

    CraftMetaSkull(CraftMetaItem meta) {
        super(meta);
        if (!(meta instanceof CraftMetaSkull skullMeta)) {
            return;
        }
        this.setProfile(skullMeta.profile);
        this.noteBlockSound = skullMeta.noteBlockSound;
    }

    CraftMetaSkull(DataComponentPatch tag, java.util.Set<net.minecraft.core.component.DataComponentType<?>> extraHandledDcts) {
        super(tag, extraHandledDcts);

        getOrEmpty(tag, CraftMetaSkull.SKULL_PROFILE).ifPresent(this::setProfile);

        getOrEmpty(tag, CraftMetaSkull.NOTE_BLOCK_SOUND).ifPresent((noteBlockSound) -> this.noteBlockSound = noteBlockSound);
    }

    CraftMetaSkull(Map<String, Object> map) {
        super(map);
        if (this.profile == null) {
            Object object = map.get(CraftMetaSkull.SKULL_OWNER.BUKKIT);
            if (object instanceof PlayerProfile playerProfile) {
                this.setOwnerProfile(playerProfile);
            } else {
                this.setOwner(SerializableMeta.getString(map, CraftMetaSkull.SKULL_OWNER.BUKKIT, true));
            }
        }

        if (this.noteBlockSound == null) {
            Object object = map.get(CraftMetaSkull.NOTE_BLOCK_SOUND.BUKKIT);
            if (object != null) {
                this.setNoteBlockSound(NamespacedKey.fromString(object.toString()));
            }
        }
    }

    @Override
    void deserializeInternal(CompoundTag tag, Object context) {
        super.deserializeInternal(tag, context);

        tag.getCompound(CraftMetaSkull.SKULL_PROFILE.NBT).ifPresent(skullTag -> {
            // convert type of stored Id from String to UUID for backwards compatibility
            skullTag.read("Id", UUIDUtil.STRING_CODEC).ifPresent(legacyId -> {
                skullTag.store("Id", UUIDUtil.CODEC, legacyId);
            });

            ResolvableProfile.CODEC.parse(NbtOps.INSTANCE, skullTag).result().ifPresent(this::setProfile);
        });

        tag.getCompound(CraftMetaSkull.BLOCK_ENTITY_TAG.NBT)
            .flatMap(blockEntityTag -> blockEntityTag.copy().getString(CraftMetaSkull.NOTE_BLOCK_SOUND.NBT))
            .ifPresent(noteBlockSound -> {
                this.noteBlockSound = ResourceLocation.tryParse(noteBlockSound);
            });
    }

    private void setProfile(ResolvableProfile profile) {
        this.profile = profile;
    }

    @Override
    void applyToItem(CraftMetaItem.Applicator tag) {
        super.applyToItem(tag);

        if (this.hasOwner()) {
            // SPIGOT-6558: Set initial textures
            tag.put(CraftMetaSkull.SKULL_PROFILE, this.profile);
            // Fill in textures
            PlayerProfile ownerProfile = new CraftPlayerProfile(this.profile); // getOwnerProfile may return null
            if (ownerProfile.getTextures().isEmpty()) {
                ownerProfile.update().thenAcceptAsync((filledProfile) -> { // Paper - run on main thread
                    this.setOwnerProfile(filledProfile);
                    tag.skullCallback(this.profile); // Paper - actually set profile on itemstack
                }, ((org.bukkit.craftbukkit.CraftServer) org.bukkit.Bukkit.getServer()).getServer()); // Paper - run on main thread
            }
        }

        if (this.noteBlockSound != null) {
            tag.put(CraftMetaSkull.NOTE_BLOCK_SOUND, this.noteBlockSound);
        }
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && this.isSkullEmpty();
    }

    boolean isSkullEmpty() {
        return this.profile == null && this.noteBlockSound == null;
    }

    @Override
    public CraftMetaSkull clone() {
        return (CraftMetaSkull) super.clone();
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
    public void setPlayerProfile(@Nullable com.destroystokyo.paper.profile.PlayerProfile profile) {
        setProfile((profile == null) ? null : com.destroystokyo.paper.profile.CraftPlayerProfile.asResolvableProfileCopy(profile));
    }

    @Nullable
    @Override
    public com.destroystokyo.paper.profile.PlayerProfile getPlayerProfile() {
        return profile != null ? new com.destroystokyo.paper.profile.CraftPlayerProfile(profile) : null;
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
    public boolean setOwner(String name) {
        if (name != null && name.length() > CraftMetaSkull.MAX_OWNER_LENGTH) {
            return false;
        }

        if (name == null) {
            this.setProfile(null);
        } else {
            // Attempt to fetch an already resolved player profile in case the player is currently online.
            net.minecraft.server.level.ServerPlayer player = net.minecraft.server.MinecraftServer.getServer().getPlayerList().getPlayerByName(name);
            this.setProfile(
                player != null
                    ? ResolvableProfile.createResolved(player.getGameProfile())
                    : new ResolvableProfile.Dynamic(Either.left(name), PlayerSkin.Patch.EMPTY)
            );
        }

        return true;
    }

    @Override
    public boolean setOwningPlayer(OfflinePlayer owner) {
        if (owner == null) {
            this.setProfile(null);
        } else if (owner instanceof CraftPlayer craftPlayer) {
            this.setProfile(ResolvableProfile.createResolved(craftPlayer.getProfile()));
        } else {
            this.setProfile(new ResolvableProfile.Dynamic(Either.right(owner.getUniqueId()), PlayerSkin.Patch.EMPTY));
        }

        return true;
    }

    @Override
    @Deprecated
    public PlayerProfile getOwnerProfile() {
        if (!this.hasOwner()) {
            return null;
        }

        return new CraftPlayerProfile(this.profile);
    }

    @Override
    @Deprecated
    public void setOwnerProfile(PlayerProfile profile) {
        if (profile instanceof final com.destroystokyo.paper.profile.SharedPlayerProfile sharedProfile) {
            this.setProfile(sharedProfile.buildResolvableProfile());
        } else {
            this.setProfile(null);
        }
    }

    @Override
    public void setNoteBlockSound(NamespacedKey noteBlockSound) {
        if (noteBlockSound == null) {
            this.noteBlockSound = null;
        } else {
            this.noteBlockSound = CraftNamespacedKey.toMinecraft(noteBlockSound);
        }
    }

    @Override
    public NamespacedKey getNoteBlockSound() {
        return (this.noteBlockSound == null) ? null : CraftNamespacedKey.fromMinecraft(this.noteBlockSound);
    }

    @Override
    int applyHash() {
        final int original;
        int hash = original = super.applyHash();
        if (this.hasOwner()) {
            hash = 61 * hash + this.profile.hashCode();
        }
        if (this.noteBlockSound != null) {
            hash = 61 * hash + this.noteBlockSound.hashCode();
        }
        return original != hash ? CraftMetaSkull.class.hashCode() ^ hash : hash;
    }

    @Override
    boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof CraftMetaSkull other) {
            return Objects.equals(this.profile, other.profile) && Objects.equals(this.noteBlockSound, other.noteBlockSound);
        }
        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaSkull || this.isSkullEmpty());
    }

    @Override
    Builder<String, Object> serialize(Builder<String, Object> builder) {
        super.serialize(builder);

        if (this.hasOwner()) {
            builder.put(CraftMetaSkull.SKULL_OWNER.BUKKIT, new com.destroystokyo.paper.profile.CraftPlayerProfile(this.profile));
        }

        NamespacedKey noteBlockSound = this.getNoteBlockSound();
        if (noteBlockSound != null) {
            builder.put(CraftMetaSkull.NOTE_BLOCK_SOUND.BUKKIT, noteBlockSound.toString());
        }

        return builder;
    }
}
