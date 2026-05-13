package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import io.papermc.paper.registry.HolderableBase;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.pig.PigSoundVariant;
import net.minecraft.world.entity.animal.pig.PigVariant;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.bukkit.Material;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Pig;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class CraftPig extends CraftAnimals implements Pig {

    public CraftPig(CraftServer server, net.minecraft.world.entity.animal.pig.Pig entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.animal.pig.Pig getHandle() {
        return (net.minecraft.world.entity.animal.pig.Pig) this.entity;
    }

    @Override
    public boolean hasSaddle() {
        return this.getHandle().isSaddled();
    }

    @Override
    public void setSaddle(boolean saddled) {
        this.getHandle().setItemSlot(EquipmentSlot.SADDLE, saddled ? new ItemStack(Items.SADDLE) : ItemStack.EMPTY);
    }

    @Override
    public int getBoostTicks() {
        return this.getHandle().steering.boosting ? this.getHandle().steering.boostTimeTotal() : 0;
    }

    @Override
    public void setBoostTicks(int ticks) {
        Preconditions.checkArgument(ticks >= 0, "ticks must be >= 0");

        this.getHandle().steering.setBoostTicks(ticks);
    }

    @Override
    public int getCurrentBoostTicks() {
        return this.getHandle().steering.boosting ? this.getHandle().steering.boostTime : 0;
    }

    @Override
    public void setCurrentBoostTicks(int ticks) {
        if (!this.getHandle().steering.boosting) {
            return;
        }

        int max = this.getHandle().steering.boostTimeTotal();
        Preconditions.checkArgument(ticks >= 0 && ticks <= max, "boost ticks must not exceed 0 or %s (inclusive)", max);

        this.getHandle().steering.boostTime = ticks;
    }

    @Override
    public Material getSteerMaterial() {
        return Material.CARROT_ON_A_STICK;
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

    public static class CraftVariant extends HolderableBase<PigVariant> implements Variant {

        public static Variant minecraftHolderToBukkit(Holder<PigVariant> minecraft) {
            return CraftRegistry.minecraftHolderToBukkit(minecraft, Registries.PIG_VARIANT);
        }

        public static Holder<PigVariant> bukkitToMinecraftHolder(Variant bukkit) {
            return CraftRegistry.bukkitToMinecraftHolder(bukkit);
        }

        public CraftVariant(final Holder<PigVariant> holder) {
            super(holder);
        }
    }

    public static class CraftSoundVariant extends HolderableBase<PigSoundVariant> implements SoundVariant {

        public static SoundVariant minecraftHolderToBukkit(Holder<PigSoundVariant> minecraft) {
            return CraftRegistry.minecraftHolderToBukkit(minecraft, Registries.PIG_SOUND_VARIANT);
        }

        public static Holder<PigSoundVariant> bukkitToMinecraftHolder(SoundVariant bukkit) {
            return CraftRegistry.bukkitToMinecraftHolder(bukkit);
        }

        public CraftSoundVariant(final Holder<PigSoundVariant> holder) {
            super(holder);
        }
    }
}
