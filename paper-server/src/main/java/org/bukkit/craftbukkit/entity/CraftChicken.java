package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.registry.HolderableBase;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.animal.ChickenVariant;
import net.minecraft.world.item.EitherHolder;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Chicken;
import org.jspecify.annotations.NullMarked;
import java.util.Optional;

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

    public static class CraftVariant extends HolderableBase<ChickenVariant> implements Variant {

        public static Variant minecraftToBukkit(ChickenVariant minecraft) {
            return CraftRegistry.minecraftToBukkit(minecraft, Registries.CHICKEN_VARIANT);
        }

        public static Variant minecraftHolderToBukkit(Holder<ChickenVariant> minecraft) {
            return CraftRegistry.minecraftHolderToBukkit(minecraft, Registries.CHICKEN_VARIANT);
        }

        public static Variant minecraftEitherHolderToBukkit(EitherHolder<ChickenVariant> minecraft) {
            final Optional<Holder<ChickenVariant>> left = minecraft.contents().left();
            if (left.isPresent()) {
                return CraftRegistry.minecraftHolderToBukkit(left.get(), Registries.CHICKEN_VARIANT);
            }
            final Optional<ResourceKey<ChickenVariant>> right = minecraft.contents().right();
            if (right.isPresent()) {
                final Holder.Reference<ChickenVariant> orThrow = CraftRegistry.getMinecraftRegistry(right.get().registryKey()).getOrThrow(right.get());
                return minecraftToBukkit(orThrow.value());
            }
            throw new IllegalStateException("Cannot map entry for " + minecraft);
        }

        public static EitherHolder<ChickenVariant> bukkitToMinecraftEitherHolder(Variant variant) {
            final Registry<ChickenVariant> chickenVariantRegistry = CraftRegistry.getMinecraftRegistry(Registries.CHICKEN_VARIANT);
            final Optional<Holder.Reference<ChickenVariant>> chickenVariantReference = chickenVariantRegistry.get(PaperAdventure.asVanilla(variant.key()));
            return chickenVariantReference.map(EitherHolder::new).orElseGet(() -> new EitherHolder<>(PaperAdventure.asVanilla(chickenVariantRegistry.key(), variant.key())));

        }

        public static ChickenVariant bukkitToMinecraft(Variant bukkit) {
            return CraftRegistry.bukkitToMinecraft(bukkit);
        }

        public static Holder<ChickenVariant> bukkitToMinecraftHolder(Variant bukkit) {
            return CraftRegistry.bukkitToMinecraftHolder(bukkit, Registries.CHICKEN_VARIANT);
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
