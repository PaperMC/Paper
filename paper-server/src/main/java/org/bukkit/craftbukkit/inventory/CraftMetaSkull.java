package org.bukkit.craftbukkit.inventory;

import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Sets;
import com.mojang.authlib.GameProfile;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import net.minecraft.SystemUtils;
import net.minecraft.nbt.GameProfileSerializer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.resources.MinecraftKey;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.inventory.CraftMetaItem.SerializableMeta;
import org.bukkit.craftbukkit.profile.CraftPlayerProfile;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;

@DelegateDeserialization(SerializableMeta.class)
class CraftMetaSkull extends CraftMetaItem implements SkullMeta {

    private static final Set<Material> SKULL_MATERIALS = Sets.newHashSet(
            Material.CREEPER_HEAD,
            Material.CREEPER_WALL_HEAD,
            Material.DRAGON_HEAD,
            Material.DRAGON_WALL_HEAD,
            Material.PIGLIN_HEAD,
            Material.PIGLIN_WALL_HEAD,
            Material.PLAYER_HEAD,
            Material.PLAYER_WALL_HEAD,
            Material.SKELETON_SKULL,
            Material.SKELETON_WALL_SKULL,
            Material.WITHER_SKELETON_SKULL,
            Material.WITHER_SKELETON_WALL_SKULL,
            Material.ZOMBIE_HEAD,
            Material.ZOMBIE_WALL_HEAD
    );

    @ItemMetaKey.Specific(ItemMetaKey.Specific.To.NBT)
    static final ItemMetaKey SKULL_PROFILE = new ItemMetaKey("SkullProfile");

    static final ItemMetaKey SKULL_OWNER = new ItemMetaKey("SkullOwner", "skull-owner");

    @ItemMetaKey.Specific(ItemMetaKey.Specific.To.NBT)
    static final ItemMetaKey BLOCK_ENTITY_TAG = new ItemMetaKey("BlockEntityTag");
    static final ItemMetaKey NOTE_BLOCK_SOUND = new ItemMetaKey("note_block_sound");
    static final int MAX_OWNER_LENGTH = 16;

    private GameProfile profile;
    private NBTTagCompound serializedProfile;
    private MinecraftKey noteBlockSound;

    CraftMetaSkull(CraftMetaItem meta) {
        super(meta);
        if (!(meta instanceof CraftMetaSkull)) {
            return;
        }
        CraftMetaSkull skullMeta = (CraftMetaSkull) meta;
        this.setProfile(skullMeta.profile);
        this.noteBlockSound = skullMeta.noteBlockSound;
    }

    CraftMetaSkull(NBTTagCompound tag) {
        super(tag);

        if (tag.contains(SKULL_OWNER.NBT, CraftMagicNumbers.NBT.TAG_COMPOUND)) {
            this.setProfile(GameProfileSerializer.readGameProfile(tag.getCompound(SKULL_OWNER.NBT)));
        } else if (tag.contains(SKULL_OWNER.NBT, CraftMagicNumbers.NBT.TAG_STRING) && !tag.getString(SKULL_OWNER.NBT).isEmpty()) {
            this.setProfile(new GameProfile(SystemUtils.NIL_UUID, tag.getString(SKULL_OWNER.NBT)));
        }

        if (tag.contains(BLOCK_ENTITY_TAG.NBT, CraftMagicNumbers.NBT.TAG_COMPOUND)) {
            NBTTagCompound nbtTagCompound = tag.getCompound(BLOCK_ENTITY_TAG.NBT).copy();
            if (nbtTagCompound.contains(NOTE_BLOCK_SOUND.NBT, 8)) {
                this.noteBlockSound = MinecraftKey.tryParse(nbtTagCompound.getString(NOTE_BLOCK_SOUND.NBT));
            }
        }
    }

    CraftMetaSkull(Map<String, Object> map) {
        super(map);
        if (profile == null) {
            Object object = map.get(SKULL_OWNER.BUKKIT);
            if (object instanceof PlayerProfile) {
                setOwnerProfile((PlayerProfile) object);
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

            this.setProfile(GameProfileSerializer.readGameProfile(skullTag));
        }

        if (tag.contains(BLOCK_ENTITY_TAG.NBT, CraftMagicNumbers.NBT.TAG_COMPOUND)) {
            NBTTagCompound nbtTagCompound = tag.getCompound(BLOCK_ENTITY_TAG.NBT).copy();
            if (nbtTagCompound.contains(NOTE_BLOCK_SOUND.NBT, 8)) {
                this.noteBlockSound = MinecraftKey.tryParse(nbtTagCompound.getString(NOTE_BLOCK_SOUND.NBT));
            }
        }
    }

    private void setProfile(GameProfile profile) {
        this.profile = profile;
        this.serializedProfile = (profile == null) ? null : GameProfileSerializer.writeGameProfile(new NBTTagCompound(), profile);
    }

    @Override
    void applyToItem(NBTTagCompound tag) {
        super.applyToItem(tag);

        if (profile != null) {
            checkForInconsistency();

            // SPIGOT-6558: Set initial textures
            tag.put(SKULL_OWNER.NBT, serializedProfile);
            // Fill in textures
            PlayerProfile ownerProfile = new CraftPlayerProfile(profile); // getOwnerProfile may return null
            if (ownerProfile.getTextures().isEmpty()) {
                ownerProfile.update().thenAccept((filledProfile) -> {
                    setOwnerProfile(filledProfile);
                    tag.put(SKULL_OWNER.NBT, serializedProfile);
                });
            }
        }

        if (noteBlockSound != null) {
            NBTTagCompound nbtTagCompound = new NBTTagCompound();
            nbtTagCompound.putString(NOTE_BLOCK_SOUND.NBT, this.noteBlockSound.toString());
            tag.put(BLOCK_ENTITY_TAG.NBT, nbtTagCompound);
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
    boolean applicableTo(Material type) {
        return SKULL_MATERIALS.contains(type);
    }

    @Override
    public CraftMetaSkull clone() {
        return (CraftMetaSkull) super.clone();
    }

    @Override
    public boolean hasOwner() {
        return profile != null && !profile.getName().isEmpty();
    }

    @Override
    public String getOwner() {
        return hasOwner() ? profile.getName() : null;
    }

    @Override
    public OfflinePlayer getOwningPlayer() {
        if (hasOwner()) {
            if (!profile.getId().equals(SystemUtils.NIL_UUID)) {
                return Bukkit.getOfflinePlayer(profile.getId());
            }

            if (!profile.getName().isEmpty()) {
                return Bukkit.getOfflinePlayer(profile.getName());
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
            setProfile(new GameProfile(SystemUtils.NIL_UUID, name));
        }

        return true;
    }

    @Override
    public boolean setOwningPlayer(OfflinePlayer owner) {
        if (owner == null) {
            setProfile(null);
        } else if (owner instanceof CraftPlayer) {
            setProfile(((CraftPlayer) owner).getProfile());
        } else {
            setProfile(new GameProfile(owner.getUniqueId(), owner.getName()));
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
        if (profile == null) {
            setProfile(null);
        } else {
            setProfile(CraftPlayerProfile.validateSkullProfile(((CraftPlayerProfile) profile).buildGameProfile()));
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
        if (meta instanceof CraftMetaSkull) {
            CraftMetaSkull that = (CraftMetaSkull) meta;

            checkForInconsistency();
            // SPIGOT-5403: equals does not check properties
            return (this.profile != null ? that.profile != null && this.serializedProfile.equals(that.serializedProfile) : that.profile == null) && Objects.equals(this.noteBlockSound, that.noteBlockSound);
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
        if (this.profile != null) {
            return builder.put(SKULL_OWNER.BUKKIT, new CraftPlayerProfile(this.profile));
        }
        NamespacedKey namespacedKeyNB = this.getNoteBlockSound();
        if (namespacedKeyNB != null) {
            return builder.put(NOTE_BLOCK_SOUND.BUKKIT, namespacedKeyNB.toString());
        }
        return builder;
    }

    private void checkForInconsistency() {
        if (profile != null && serializedProfile == null) {
            // SPIGOT-7510: Fix broken reflection usage from plugins
            Bukkit.getLogger().warning("""
                    Found inconsistent skull meta, this should normally not happen and is not a Bukkit / Spigot issue, but one from a plugin you are using.
                    Bukkit will attempt to fix it this time for you, but may not be able to do this every time.
                    If you see this message after typing a command from a plugin, please report this to the plugin developer, they should use the api instead of relying on reflection (and doing it the wrong way).""");
            serializedProfile = GameProfileSerializer.writeGameProfile(new NBTTagCompound(), profile);
        }
    }
}
