package org.bukkit.craftbukkit.inventory;

import com.google.common.collect.ImmutableMap.Builder;
import com.mojang.authlib.GameProfile;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.Util;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceLocation;
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

    CraftMetaSkull(DataComponentPatch tag, java.util.Set<net.minecraft.core.component.DataComponentType<?>> extraHandledDcts) { // Paper
        super(tag, extraHandledDcts); // Paper

        getOrEmpty(tag, CraftMetaSkull.SKULL_PROFILE).ifPresent(this::setProfile);

        getOrEmpty(tag, CraftMetaSkull.NOTE_BLOCK_SOUND).ifPresent((minecraftKey) -> this.noteBlockSound = minecraftKey);
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
            Optional<String> legacyId = skullTag.getString("Id");
            if (legacyId.isPresent()) {
                skullTag.store("Id", UUIDUtil.CODEC, UUID.fromString(legacyId.get()));
            }

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

    // Paper start
    @Override
    public void setPlayerProfile(@org.jetbrains.annotations.Nullable com.destroystokyo.paper.profile.PlayerProfile profile) {
        setProfile((profile == null) ? null : com.destroystokyo.paper.profile.CraftPlayerProfile.asResolvableProfileCopy(profile));
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public com.destroystokyo.paper.profile.PlayerProfile getPlayerProfile() {
        return profile != null ? new com.destroystokyo.paper.profile.CraftPlayerProfile(profile) : null;
    }
    // Paper end

    @Override
    public OfflinePlayer getOwningPlayer() {
        if (this.hasOwner()) {
            if (this.profile.id().filter(u -> !u.equals(Util.NIL_UUID)).isPresent()) {
                return Bukkit.getOfflinePlayer(this.profile.id().get());
            }

            if (this.profile.name().filter(s -> !s.isEmpty()).isPresent()) {
                return Bukkit.getOfflinePlayer(this.profile.name().get());
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
            // Paper start - Use Online Players Skull
            GameProfile newProfile = null;
            net.minecraft.server.level.ServerPlayer player = net.minecraft.server.MinecraftServer.getServer().getPlayerList().getPlayerByName(name);
            if (player != null) newProfile = player.getGameProfile();
            if (newProfile == null) newProfile = new GameProfile(Util.NIL_UUID, name);
            this.setProfile(new ResolvableProfile(newProfile));
            // Paper end
        }

        return true;
    }

    @Override
    public boolean setOwningPlayer(OfflinePlayer owner) {
        if (owner == null) {
            this.setProfile(null);
        } else if (owner instanceof CraftPlayer craftPlayer) {
            this.setProfile(new ResolvableProfile(craftPlayer.getProfile()));
        } else {
            this.setProfile(new ResolvableProfile(new GameProfile(owner.getUniqueId(), (owner.getName() == null) ? "" : owner.getName())));
        }

        return true;
    }

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
        if (profile instanceof final com.destroystokyo.paper.profile.SharedPlayerProfile sharedProfile) {
            this.setProfile(CraftPlayerProfile.validateSkullProfile(sharedProfile.buildResolvableProfile())); // Paper
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
        if (meta instanceof CraftMetaSkull that) {
            return Objects.equals(this.profile, that.profile) && Objects.equals(this.noteBlockSound, that.noteBlockSound);
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
            builder.put(CraftMetaSkull.SKULL_OWNER.BUKKIT, new com.destroystokyo.paper.profile.CraftPlayerProfile(this.profile)); // Paper
        }

        NamespacedKey namespacedKeyNB = this.getNoteBlockSound();
        if (namespacedKeyNB != null) {
            builder.put(CraftMetaSkull.NOTE_BLOCK_SOUND.BUKKIT, namespacedKeyNB.toString());
        }

        return builder;
    }
}
