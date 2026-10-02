package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import io.papermc.paper.registry.HolderableBase;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.animal.cow.CowSoundVariant;
import net.minecraft.world.entity.animal.cow.CowVariant;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Cow;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class CraftCow extends CraftAbstractCow implements Cow {

    public CraftCow(CraftServer server, net.minecraft.world.entity.animal.cow.Cow entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.animal.cow.Cow getHandle() {
        return (net.minecraft.world.entity.animal.cow.Cow) this.entity;
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
    public void setSoundVariant(SoundVariant variant) {
        Preconditions.checkArgument(variant != null, "variant cannot be null");

        this.getHandle().setSoundVariant(CraftSoundVariant.bukkitToMinecraftHolder(variant));
    }

    public static class CraftVariant extends HolderableBase<CowVariant> implements Variant {

        public static Variant minecraftHolderToBukkit(Holder<CowVariant> minecraft) {
            return CraftRegistry.minecraftHolderToBukkit(minecraft, Registries.COW_VARIANT);
        }

        public static Holder<CowVariant> bukkitToMinecraftHolder(Variant bukkit) {
            return CraftRegistry.bukkitToMinecraftHolder(bukkit);
        }

        public CraftVariant(final Holder<CowVariant> holder) {
            super(holder);
        }
    }

    public static class CraftSoundVariant extends HolderableBase<CowSoundVariant> implements SoundVariant {

        public static SoundVariant minecraftHolderToBukkit(Holder<CowSoundVariant> minecraft) {
            return CraftRegistry.minecraftHolderToBukkit(minecraft, Registries.COW_SOUND_VARIANT);
        }

        public static Holder<CowSoundVariant> bukkitToMinecraftHolder(SoundVariant bukkit) {
            return CraftRegistry.bukkitToMinecraftHolder(bukkit);
        }

        public CraftSoundVariant(final Holder<CowSoundVariant> holder) {
            super(holder);
        }
    }
}
