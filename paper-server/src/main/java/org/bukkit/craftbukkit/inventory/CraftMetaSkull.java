package org.bukkit.craftbukkit.inventory;

import com.google.common.collect.ImmutableMap.Builder;
import com.mojang.authlib.GameProfile;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import net.minecraft.SystemUtils;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.DynamicOpsNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.world.item.component.ResolvableProfile;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.profile.CraftPlayerProfile;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
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
    static final ItemMetaKeyType<MinecraftKey> NOTE_BLOCK_SOUND = new ItemMetaKeyType<>(DataComponents.NOTE_BLOCK_SOUND, "note_block_sound");
    static final int MAX_OWNER_LENGTH = 16;

    private ResolvableProfile profile;
    private MinecraftKey noteBlockSound;

    CraftMetaSkull(CraftMetaItem meta) {
        super(meta);
        if (!(meta instanceof CraftMetaSkull skullMeta)) {
            return;
        }
        this.setProfile(skullMeta.profile);
        this.noteBlockSound = skullMeta.noteBlockSound;
    }

    CraftMetaSkull(DataComponentPatch tag) {
        super(tag);

        getOrEmpty(tag, SKULL_PROFILE).ifPresent(this::setProfile);

        getOrEmpty(tag, NOTE_BLOCK_SOUND).ifPresent((minecraftKey) -> this.noteBlockSound = minecraftKey);
    }

    CraftMetaSkull(Map<String, Object> map) {
        super(map);
        if (profile == null) {
            Object object = map.get(SKULL_OWNER.BUKKIT);
            if (object instanceof PlayerProfile playerProfile) {
                setOwnerProfile(playerProfile);
            } else {
                setOwner(SerializableMeta.getString(map, SKULL_OWNER.BUKKIT, true));
            }
        }

        if (noteBlockSound == null) {
            Object object = map.get(NOTE_BLOCK_SOUND.BUKKIT);
            if (object != null) {
                setNoteBlockSound(NamespacedKey.fromString(object.toString()));
            }
        }
    }

    @Override
    void deserializeInternal(NBTTagCompound tag, Object context) {
        super.deserializeInternal(tag, context);

        if (tag.contains(SKULL_PROFILE.NBT, CraftMagicNumbers.NBT.TAG_COMPOUND)) {
            NBTTagCompound skullTag = tag.getCompound(SKULL_PROFILE.NBT);
            // convert type of stored Id from String to UUID for backwards compatibility
            if (skullTag.contains("Id", CraftMagicNumbers.NBT.TAG_STRING)) {
                UUID uuid = UUID.fromString(skullTag.getString("Id"));
                skullTag.putUUID("Id", uuid);
            }

            ResolvableProfile.CODEC.parse(DynamicOpsNBT.INSTANCE, skullTag).result().ifPresent(this::setProfile);
        }

        if (tag.contains(BLOCK_ENTITY_TAG.NBT, CraftMagicNumbers.NBT.TAG_COMPOUND)) {
            NBTTagCompound nbtTagCompound = tag.getCompound(BLOCK_ENTITY_TAG.NBT).copy();
            if (nbtTagCompound.contains(NOTE_BLOCK_SOUND.NBT, 8)) {
                this.noteBlockSound = MinecraftKey.tryParse(nbtTagCompound.getString(NOTE_BLOCK_SOUND.NBT));
            }
        }
    }

    private void setProfile(ResolvableProfile profile) {
        this.profile = profile;
    }

    @Override
    void applyToItem(CraftMetaItem.Applicator tag) {
        super.applyToItem(tag);

        if (hasOwner()) {
            // SPIGOT-6558: Set initial textures
            tag.put(SKULL_PROFILE, profile);
            // Fill in textures
            PlayerProfile ownerProfile = new CraftPlayerProfile(profile); // getOwnerProfile may return null
            if (ownerProfile.getTextures().isEmpty()) {
                ownerProfile.update().thenAccept((filledProfile) -> {
                    setOwnerProfile(filledProfile);
                    tag.put(SKULL_PROFILE, profile);
                });
            }
        }

        if (noteBlockSound != null) {
            tag.put(NOTE_BLOCK_SOUND, this.noteBlockSound);
        }
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && isSkullEmpty();
    }

    boolean isSkullEmpty() {
        return profile == null && noteBlockSound == null;
    }

    @Override
    public CraftMetaSkull clone() {
        return (CraftMetaSkull) super.clone();
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
    public boolean setOwner(String name) {
        if (name != null && name.length() > MAX_OWNER_LENGTH) {
            return false;
        }

        if (name == null) {
            setProfile(null);
        } else {
            setProfile(new ResolvableProfile(new GameProfile(SystemUtils.NIL_UUID, name)));
        }

        return true;
    }

    @Override
    public boolean setOwningPlayer(OfflinePlayer owner) {
        if (owner == null) {
            setProfile(null);
        } else if (owner instanceof CraftPlayer craftPlayer) {
            setProfile(new ResolvableProfile(craftPlayer.getProfile()));
        } else {
            setProfile(new ResolvableProfile(new GameProfile(owner.getUniqueId(), (owner.getName() == null) ? "" : owner.getName())));
        }

        return true;
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
        if (profile instanceof CraftPlayerProfile craftPlayerProfile) {
            setProfile(CraftPlayerProfile.validateSkullProfile(craftPlayerProfile.buildResolvableProfile()));
        } else {
            setProfile(null);
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
        if (hasOwner()) {
            hash = 61 * hash + profile.hashCode();
        }
        if (this.noteBlockSound != null) {
            hash = 61 * hash + noteBlockSound.hashCode();
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
        return super.notUncommon(meta) && (meta instanceof CraftMetaSkull || isSkullEmpty());
    }

    @Override
    Builder<String, Object> serialize(Builder<String, Object> builder) {
        super.serialize(builder);

        if (this.hasOwner()) {
            builder.put(SKULL_OWNER.BUKKIT, new CraftPlayerProfile(this.profile));
        }

        NamespacedKey namespacedKeyNB = this.getNoteBlockSound();
        if (namespacedKeyNB != null) {
            builder.put(NOTE_BLOCK_SOUND.BUKKIT, namespacedKeyNB.toString());
        }

        return builder;
    }
}
