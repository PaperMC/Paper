package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.animal.ChickenVariant;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.util.Handleable;
import org.bukkit.entity.Chicken;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class CraftChicken extends CraftAnimals implements Chicken {

    public CraftChicken(CraftServer server, net.minecraft.world.entity.animal.Chicken entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.animal.Chicken getHandle() {
        return (net.minecraft.world.entity.animal.Chicken) this.entity;
    }

    @Override
    public String toString() {
        return "CraftChicken";
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

    public static class CraftVariant implements Variant, Handleable<ChickenVariant> {

        public static Variant minecraftToBukkit(ChickenVariant minecraft) {
            return CraftRegistry.minecraftToBukkit(minecraft, Registries.CHICKEN_VARIANT);
        }

        public static Variant minecraftHolderToBukkit(Holder<ChickenVariant> minecraft) {
            return CraftVariant.minecraftToBukkit(minecraft.value());
        }

        public static ChickenVariant bukkitToMinecraft(Variant bukkit) {
            return CraftRegistry.bukkitToMinecraft(bukkit);
        }

        public static Holder<ChickenVariant> bukkitToMinecraftHolder(Variant bukkit) {
            Preconditions.checkArgument(bukkit != null);

            net.minecraft.core.Registry<ChickenVariant> registry = CraftRegistry.getMinecraftRegistry(Registries.CHICKEN_VARIANT);

            if (registry.wrapAsHolder(CraftVariant.bukkitToMinecraft(bukkit)) instanceof Holder.Reference<ChickenVariant> holder) {
                return holder;
            }

            throw new IllegalArgumentException("No Reference holder found for " + bukkit
                + ", this can happen if a plugin creates its own chicken variant with out properly registering it.");
        }

        private final NamespacedKey key;
        private final ChickenVariant variant;

        public CraftVariant(NamespacedKey key, ChickenVariant variant) {
            this.key = key;
            this.variant = variant;
        }

        @Override
        public ChickenVariant getHandle() {
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
    public boolean isChickenJockey() {
        return this.getHandle().isChickenJockey();
    }

    @Override
    public void setIsChickenJockey(boolean isChickenJockey) {
        this.getHandle().setChickenJockey(isChickenJockey);
    }

    @Override
    public int getEggLayTime() {
        return this.getHandle().eggTime;
    }

    @Override
    public void setEggLayTime(int eggLayTime) {
        this.getHandle().eggTime = eggLayTime;
    }
}
