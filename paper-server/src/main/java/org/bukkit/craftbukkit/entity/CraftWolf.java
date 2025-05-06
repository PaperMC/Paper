package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.animal.wolf.WolfSoundVariant;
import net.minecraft.world.entity.animal.wolf.WolfVariant;
import org.bukkit.DyeColor;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.util.Handleable;
import org.bukkit.entity.Wolf;

public class CraftWolf extends CraftTameableAnimal implements Wolf {

    public CraftWolf(CraftServer server, net.minecraft.world.entity.animal.wolf.Wolf wolf) {
        super(server, wolf);
    }

    @Override
    public net.minecraft.world.entity.animal.wolf.Wolf getHandle() {
        return (net.minecraft.world.entity.animal.wolf.Wolf) this.entity;
    }

    @Override
    public boolean isAngry() {
        return this.getHandle().isAngry();
    }

    @Override
    public void setAngry(boolean angry) {
        if (angry) {
            this.getHandle().startPersistentAngerTimer();
        } else {
            this.getHandle().stopBeingAngry();
        }
    }

    @Override
    public DyeColor getCollarColor() {
        return DyeColor.getByWoolData((byte) this.getHandle().getCollarColor().getId());
    }

    @Override
    public void setCollarColor(DyeColor color) {
        this.getHandle().setCollarColor(net.minecraft.world.item.DyeColor.byId(color.getWoolData()));
    }

    @Override
    public boolean isWet() {
        return this.getHandle().isWet;
    }

    @Override
    public float getTailAngle() {
        return this.getHandle().getTailAngle();
    }

    @Override
    public boolean isInterested() {
        return this.getHandle().isInterested();
    }

    @Override
    public void setInterested(boolean interested) {
        this.getHandle().setIsInterested(interested);
    }

    @Override
    public Variant getVariant() {
        return CraftVariant.minecraftHolderToBukkit(this.getHandle().getVariant());
    }

    @Override
    public void setVariant(Variant variant) {
        Preconditions.checkArgument(variant != null, "variant cannot be null");

        this.getHandle().setVariant(CraftVariant.bukkitToMinecraftHolder(variant));
    }

    @Override
    public SoundVariant getSoundVariant() {
        return CraftSoundVariant.minecraftHolderToBukkit(this.getHandle().getSoundVariant());
    }

    @Override
    public void setSoundVariant(SoundVariant soundVariant) {
        Preconditions.checkArgument(soundVariant != null, "soundVariant cannot be null");

        this.getHandle().setSoundVariant(CraftSoundVariant.bukkitToMinecraftHolder(soundVariant));
    }

    public static class CraftVariant implements Variant, Handleable<WolfVariant> {

        public static Variant minecraftToBukkit(WolfVariant minecraft) {
            return CraftRegistry.minecraftToBukkit(minecraft, Registries.WOLF_VARIANT);
        }

        public static Variant minecraftHolderToBukkit(Holder<WolfVariant> minecraft) {
            return CraftVariant.minecraftToBukkit(minecraft.value());
        }

        public static WolfVariant bukkitToMinecraft(Variant bukkit) {
            return CraftRegistry.bukkitToMinecraft(bukkit);
        }

        public static Holder<WolfVariant> bukkitToMinecraftHolder(Variant bukkit) {
            Preconditions.checkArgument(bukkit != null);

            net.minecraft.core.Registry<WolfVariant> registry = CraftRegistry.getMinecraftRegistry(Registries.WOLF_VARIANT);

            if (registry.wrapAsHolder(CraftVariant.bukkitToMinecraft(bukkit)) instanceof Holder.Reference<WolfVariant> holder) {
                return holder;
            }

            throw new IllegalArgumentException("No Reference holder found for " + bukkit
                    + ", this can happen if a plugin creates its own wolf variant with out properly registering it.");
        }

        private final NamespacedKey key;
        private final WolfVariant variant;

        public CraftVariant(NamespacedKey key, WolfVariant variant) {
            this.key = key;
            this.variant = variant;
        }

        @Override
        public WolfVariant getHandle() {
            return this.variant;
        }

        @Override
        public NamespacedKey getKey() {
            return this.key;
        }

        @Override
        public String toString() {
            return this.key.toString();
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }

            if (!(other instanceof CraftVariant otherVariant)) {
                return false;
            }

            return this.getKey().equals(otherVariant.getKey());
        }

        @Override
        public int hashCode() {
            return this.getKey().hashCode();
        }
    }

    public static class CraftSoundVariant implements SoundVariant, Handleable<WolfSoundVariant> {

        public static SoundVariant minecraftToBukkit(WolfSoundVariant minecraft) {
            return CraftRegistry.minecraftToBukkit(minecraft, Registries.WOLF_SOUND_VARIANT);
        }

        public static SoundVariant minecraftHolderToBukkit(Holder<WolfSoundVariant> minecraft) {
            return CraftSoundVariant.minecraftToBukkit(minecraft.value());
        }

        public static WolfSoundVariant bukkitToMinecraft(SoundVariant bukkit) {
            return CraftRegistry.bukkitToMinecraft(bukkit);
        }

        public static Holder<WolfSoundVariant> bukkitToMinecraftHolder(SoundVariant bukkit) {
            Preconditions.checkArgument(bukkit != null);

            net.minecraft.core.Registry<WolfSoundVariant> registry = CraftRegistry.getMinecraftRegistry(Registries.WOLF_SOUND_VARIANT);

            if (registry.wrapAsHolder(CraftSoundVariant.bukkitToMinecraft(bukkit)) instanceof Holder.Reference<WolfSoundVariant> holder) {
                return holder;
            }

            throw new IllegalArgumentException("No Reference holder found for " + bukkit
                                               + ", this can happen if a plugin creates its own wolf sound variant with out properly registering it.");
        }

        private final NamespacedKey key;
        private final WolfSoundVariant soundVariant;

        public CraftSoundVariant(NamespacedKey key, WolfSoundVariant soundVariant) {
            this.key = key;
            this.soundVariant = soundVariant;
        }

        @Override
        public WolfSoundVariant getHandle() {
            return this.soundVariant;
        }

        @Override
        public NamespacedKey getKey() {
            return this.key;
        }

        @Override
        public String toString() {
            return this.key.toString();
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }

            if (!(other instanceof CraftSoundVariant otherVariant)) {
                return false;
            }

            return this.getKey().equals(otherVariant.getKey());
        }

        @Override
        public int hashCode() {
            return this.getKey().hashCode();
        }
    }
}
