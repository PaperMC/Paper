package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import io.papermc.paper.registry.HolderableBase;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.animal.ChickenVariant;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.CraftServer;
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
    public Variant getVariant() {
        return CraftVariant.minecraftHolderToBukkit(this.getHandle().getVariant());
    }

    @Override
    public void setVariant(Variant variant) {
        Preconditions.checkArgument(variant != null, "variant cannot be null");

        this.getHandle().setVariant(CraftVariant.bukkitToMinecraftHolder(variant));
    }

    public static class CraftVariant extends HolderableBase<ChickenVariant> implements Variant {

        public static Variant minecraftToBukkit(ChickenVariant minecraft) {
            return CraftRegistry.minecraftToBukkit(minecraft, Registries.CHICKEN_VARIANT);
        }

        public static Variant minecraftHolderToBukkit(Holder<ChickenVariant> minecraft) {
            return CraftRegistry.minecraftHolderToBukkit(minecraft, Registries.CHICKEN_VARIANT);
        }

        public static ChickenVariant bukkitToMinecraft(Variant bukkit) {
            return CraftRegistry.bukkitToMinecraft(bukkit);
        }

        public static Holder<ChickenVariant> bukkitToMinecraftHolder(Variant bukkit) {
            return CraftRegistry.bukkitToMinecraftHolder(bukkit);
        }


        public CraftVariant(Holder<ChickenVariant> holder) {
            super(holder);
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
