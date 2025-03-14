package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.animal.PigVariant;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.util.Handleable;
import org.bukkit.entity.Pig;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class CraftPig extends CraftAnimals implements Pig {

    public CraftPig(CraftServer server, net.minecraft.world.entity.animal.Pig entity) {
        super(server, entity);
    }

    @Override
    public boolean hasSaddle() {
        return this.getHandle().isSaddled();
    }

    @Override
    public void setSaddle(boolean saddled) {
        this.getHandle().steering.setSaddle(saddled);
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

    public static class CraftVariant implements Variant, Handleable<PigVariant> {

        public static Variant minecraftToBukkit(PigVariant minecraft) {
            return CraftRegistry.minecraftToBukkit(minecraft, Registries.PIG_VARIANT);
        }

        public static Variant minecraftHolderToBukkit(Holder<PigVariant> minecraft) {
            return CraftVariant.minecraftToBukkit(minecraft.value());
        }

        public static PigVariant bukkitToMinecraft(Variant bukkit) {
            return CraftRegistry.bukkitToMinecraft(bukkit);
        }

        public static Holder<PigVariant> bukkitToMinecraftHolder(Variant bukkit) {
            Preconditions.checkArgument(bukkit != null);

            net.minecraft.core.Registry<PigVariant> registry = CraftRegistry.getMinecraftRegistry(Registries.PIG_VARIANT);

            if (registry.wrapAsHolder(CraftVariant.bukkitToMinecraft(bukkit)) instanceof Holder.Reference<PigVariant> holder) {
                return holder;
            }

            throw new IllegalArgumentException("No Reference holder found for " + bukkit
                + ", this can happen if a plugin creates its own pig variant with out properly registering it.");
        }

        private final NamespacedKey key;
        private final PigVariant variant;

        public CraftVariant(NamespacedKey key, PigVariant variant) {
            this.key = key;
            this.variant = variant;
        }

        @Override
        public PigVariant getHandle() {
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

    @Override
    public net.minecraft.world.entity.animal.Pig getHandle() {
        return (net.minecraft.world.entity.animal.Pig) this.entity;
    }

    @Override
    public String toString() {
        return "CraftPig";
    }
}
