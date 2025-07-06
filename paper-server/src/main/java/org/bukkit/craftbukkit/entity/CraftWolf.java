package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import io.papermc.paper.registry.HolderableBase;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.animal.wolf.WolfSoundVariant;
import net.minecraft.world.entity.animal.wolf.WolfVariant;
import org.bukkit.DyeColor;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.CraftServer;
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

    public static class CraftVariant extends HolderableBase<WolfVariant> implements Variant {

        public static Variant minecraftToBukkit(WolfVariant minecraft) {
            return CraftRegistry.minecraftToBukkit(minecraft, Registries.WOLF_VARIANT);
        }

        public static Variant minecraftHolderToBukkit(Holder<WolfVariant> minecraft) {
            return CraftRegistry.minecraftHolderToBukkit(minecraft, Registries.WOLF_VARIANT);
        }

        public static WolfVariant bukkitToMinecraft(Variant bukkit) {
            return CraftRegistry.bukkitToMinecraft(bukkit);
        }

        public static Holder<WolfVariant> bukkitToMinecraftHolder(Variant bukkit) {
            return CraftRegistry.bukkitToMinecraftHolder(bukkit);
        }

        public CraftVariant(final Holder<WolfVariant> holder) {
            super(holder);
        }
    }

    public static class CraftSoundVariant extends HolderableBase<WolfSoundVariant> implements SoundVariant {

        public static SoundVariant minecraftToBukkit(WolfSoundVariant minecraft) {
            return CraftRegistry.minecraftToBukkit(minecraft, Registries.WOLF_SOUND_VARIANT);
        }

        public static SoundVariant minecraftHolderToBukkit(Holder<WolfSoundVariant> minecraft) {
            return CraftRegistry.minecraftHolderToBukkit(minecraft, Registries.WOLF_SOUND_VARIANT);
        }

        public static WolfSoundVariant bukkitToMinecraft(SoundVariant bukkit) {
            return CraftRegistry.bukkitToMinecraft(bukkit);
        }

        public static Holder<WolfSoundVariant> bukkitToMinecraftHolder(SoundVariant bukkit) {
            return CraftRegistry.bukkitToMinecraftHolder(bukkit);
        }

        public CraftSoundVariant(final Holder<WolfSoundVariant> holder) {
            super(holder);
        }
    }
}
